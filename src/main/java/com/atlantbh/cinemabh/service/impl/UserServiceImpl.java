package com.atlantbh.cinemabh.service.impl;

import com.atlantbh.cinemabh.dto.request.user.*;
import com.atlantbh.cinemabh.dto.response.AuthResponse;
import com.atlantbh.cinemabh.dto.response.UserDetailsResponse;
import com.atlantbh.cinemabh.entity.User;
import com.atlantbh.cinemabh.entity.VerificationCode;
import com.atlantbh.cinemabh.enums.AuthEventOutcome;
import com.atlantbh.cinemabh.enums.AuthEventType;
import com.atlantbh.cinemabh.enums.UserRole;
import com.atlantbh.cinemabh.enums.VerificationType;
import com.atlantbh.cinemabh.exception.InvalidRequestException;
import com.atlantbh.cinemabh.exception.UnauthorizedException;
import com.atlantbh.cinemabh.exception.UserNotVerifiedException;
import com.atlantbh.cinemabh.logging.AuthEvent;
import com.atlantbh.cinemabh.mapper.UserMapper;
import com.atlantbh.cinemabh.repository.CityRepository;
import com.atlantbh.cinemabh.repository.UserRepository;
import com.atlantbh.cinemabh.repository.VerificationCodeRepository;
import com.atlantbh.cinemabh.service.*;
import com.atlantbh.cinemabh.util.AuthUtils;
import com.atlantbh.cinemabh.validator.ImageUrlValidator;
import com.atlantbh.cinemabh.validator.PhoneNumberValidator;
import com.atlantbh.cinemabh.validator.ReservedNameValidator;
import com.atlantbh.cinemabh.validator.email.EmailValidator;
import com.atlantbh.cinemabh.validator.password.PasswordComplexityValidator;
import com.atlantbh.cinemabh.validator.password.PwnedPasswordValidator;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final CityRepository cityRepository;
  private final VerificationCodeRepository verificationCodeRepository;
  private final PasswordComplexityValidator passwordComplexityValidator;
  private final PwnedPasswordValidator pwnedPasswordValidator;
  private final PhoneNumberValidator phoneNumberValidator;
  private final ImageUrlValidator imageUrlValidator;
  private final PasswordEncoder passwordEncoder;
  private final UserMapper userMapper;
  private final JwtService jwtService;
  private final RefreshTokenService refreshTokenService;
  private final EmailSendingService emailSendingService;
  private final VerificationCodeService verificationCodeService;
  private final EmailValidator emailValidator;
  private final ReservedNameValidator reservedNameValidator;

  @Override
  @Transactional
  public UserDetailsResponse registerUser(RegisterUserRequest request) {
    log.info(
        "{}",
        AuthEvent.builder(AuthEventType.REGISTER, AuthEventOutcome.IN_PROGRESS)
            .email(request.email())
            .detail("Initialized register")
            .build());

    passwordComplexityValidator.validate(request.password());
    reservedNameValidator.validate(request.firstName(), request.lastName());
    pwnedPasswordValidator.validatePasswordPwned(request.password());
    emailValidator.validateEmail(request.email());

    userRepository
        .findByEmail(request.email())
        .ifPresent(
            user -> {
              log.info(
                  "{}",
                  AuthEvent.builder(AuthEventType.REGISTER, AuthEventOutcome.FAILURE)
                      .email(request.email())
                      .detail("User submitted an email which is in use")
                      .build());
              throw new InvalidRequestException("Email already taken");
            });

    User user =
        userMapper.toEntity(
            request, passwordEncoder.encode(request.password()), UserRole.REGISTERED_USER, false);

    User savedUser = userRepository.save(user);

    String verificationCode =
        verificationCodeService.generateAndSaveCode(user.getId(), VerificationType.REGISTER);

    emailSendingService.sendVerificationEmail(
        user.getEmail(), "Email verification code", verificationCode);

    log.info(
        "{}",
        AuthEvent.builder(AuthEventType.REGISTER, AuthEventOutcome.SUCCESS)
            .userId(savedUser.getId())
            .build());

    return userMapper.toDetailsResponse(savedUser);
  }

  @Override
  @Transactional
  public AuthResponse login(LoginRequest request) {
    log.info(
        "{}",
        AuthEvent.builder(AuthEventType.LOGIN, AuthEventOutcome.IN_PROGRESS)
            .email(request.email())
            .detail("Initialized login")
            .build());

    User user =
        userRepository
            .findByEmail(request.email())
            .orElseThrow(
                () -> {
                  log.info(
                      "{}",
                      AuthEvent.builder(AuthEventType.LOGIN, AuthEventOutcome.FAILURE)
                          .email(request.email())
                          .detail("User tried to log in with nonexistent email")
                          .build());
                  return new UnauthorizedException("Invalid email or password");
                });

    if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
      log.warn(
          "{}",
          AuthEvent.builder(AuthEventType.LOGIN, AuthEventOutcome.FAILURE)
              .email(request.email())
              .detail("User tried to login with wrong password")
              .build());
      throw new UnauthorizedException("Invalid email or password");
    }

    if (!user.isVerified()) {
      log.info(
          "{}",
          AuthEvent.builder(AuthEventType.LOGIN, AuthEventOutcome.FAILURE)
              .email(request.email())
              .detail("User tried to login with unverified account")
              .build());

      Optional<VerificationCode> existingCode =
          verificationCodeRepository.findByUserIdAndVerificationType(
              user.getId(), VerificationType.REGISTER);

      Instant resendAllowedAt =
          existingCode
              .map(
                  code ->
                      AuthUtils.getResendAt(
                          code.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant()))
              .orElseGet(Instant::now);

      throw new UserNotVerifiedException("Please verify your account", resendAllowedAt);
    }

    String jwtToken = jwtService.generateJwt(user);
    String refreshToken = jwtService.generateRefreshToken(user);

    refreshTokenService.hashAndSaveRefreshToken(refreshToken);

    log.info(
        "{}",
        AuthEvent.builder(AuthEventType.LOGIN, AuthEventOutcome.SUCCESS)
            .userId(user.getId())
            .detail("Logged in")
            .build());

    return new AuthResponse(jwtToken, refreshToken, userMapper.toDetailsResponse(user));
  }

  @Override
  @Transactional
  public AuthResponse refresh(String refreshToken) {
    log.info(
        "{}",
        AuthEvent.builder(AuthEventType.REFRESH_TOKEN_VALIDATION, AuthEventOutcome.IN_PROGRESS)
            .detail("Initialized refresh token validation")
            .build());

    if (!refreshTokenService.isRefreshTokenValid(refreshToken)) {
      log.warn(
          "{}",
          AuthEvent.builder(AuthEventType.REFRESH_TOKEN_VALIDATION, AuthEventOutcome.FAILURE)
              .detail("Invalid refresh token submitted")
              .build());
      throw new UnauthorizedException("Invalid refresh token");
    }

    Long userId = jwtService.extractUserId(refreshToken);

    User user =
        userRepository
            .findById(userId)
            .orElseThrow(
                () -> {
                  log.warn(
                      "{}",
                      AuthEvent.builder(
                              AuthEventType.REFRESH_TOKEN_VALIDATION, AuthEventOutcome.FAILURE)
                          .detail("User not found by id from refresh token:" + userId)
                          .build());
                  return new UnauthorizedException("Invalid refresh token");
                });

    String jwtToken = jwtService.generateJwt(user);
    String refresh = jwtService.generateRefreshToken(user);

    refreshTokenService.hashAndSaveRefreshToken(refreshToken);

    log.info(
        "{}",
        AuthEvent.builder(AuthEventType.REFRESH_TOKEN_VALIDATION, AuthEventOutcome.SUCCESS)
            .detail("Succeeded refresh token validation")
            .build());

    return new AuthResponse(jwtToken, refresh, userMapper.toDetailsResponse(user));
  }

  @Override
  @Transactional
  public AuthResponse verifyAccount(VerificationRequest request) {
    log.info(
        "{}",
        AuthEvent.builder(AuthEventType.VERIFY_ACCOUNT, AuthEventOutcome.IN_PROGRESS)
            .email(request.email())
            .detail("Initialized account verification")
            .build());

    User user =
        userRepository
            .findByEmail(request.email())
            .orElseThrow(
                () -> {
                  log.info(
                      "{}",
                      AuthEvent.builder(AuthEventType.VERIFY_ACCOUNT, AuthEventOutcome.FAILURE)
                          .email(request.email())
                          .detail("User tried to verify account with nonexistent email")
                          .build());
                  return new InvalidRequestException("Invalid email");
                });

    if (user.isVerified()) {
      log.info(
          "{}",
          AuthEvent.builder(AuthEventType.VERIFY_ACCOUNT, AuthEventOutcome.FAILURE)
              .email(request.email())
              .detail("User tried to verify already verified account")
              .build());
      throw new InvalidRequestException("Your account is already verified");
    }

    if (!verificationCodeService.isCodeValid(
        request.verificationCode(), user.getId(), VerificationType.REGISTER)) {
      throw new UnauthorizedException("Invalid or expired code");
    }

    user.setVerified(true);
    userRepository.save(user);

    String jwtToken = jwtService.generateJwt(user);
    String refreshToken = jwtService.generateRefreshToken(user);

    refreshTokenService.hashAndSaveRefreshToken(refreshToken);

    log.info(
        "{}",
        AuthEvent.builder(AuthEventType.VERIFY_ACCOUNT, AuthEventOutcome.SUCCESS)
            .userId(user.getId())
            .email(user.getEmail())
            .detail("Succeeded account verification")
            .build());

    return new AuthResponse(jwtToken, refreshToken, userMapper.toDetailsResponse(user));
  }

  @Override
  @Transactional
  public void requestPasswordReset(PasswordResetRequest request) {
    log.info(
        "{}",
        AuthEvent.builder(AuthEventType.REQUEST_RESET_PASSWORD, AuthEventOutcome.IN_PROGRESS)
            .email(request.email())
            .detail("Initialized request password reset")
            .build());

    User user =
        userRepository
            .findByEmail(request.email())
            .orElseThrow(
                () -> {
                  log.info(
                      "{}",
                      AuthEvent.builder(
                              AuthEventType.REQUEST_RESET_PASSWORD, AuthEventOutcome.FAILURE)
                          .email(request.email())
                          .detail("User requested password reset with nonexistent email")
                          .build());
                  return new InvalidRequestException("Invalid email");
                });

    if (!user.isVerified()) {
      log.info(
          "{}",
          AuthEvent.builder(AuthEventType.REQUEST_RESET_PASSWORD, AuthEventOutcome.FAILURE)
              .email(request.email())
              .detail("User requested password reset with unverified account")
              .build());
      throw new UnauthorizedException("Please verify your account");
    }

    if (verificationCodeService.isCodeSentAndValid(user.getId(), VerificationType.PASSWORD_RESET)) {
      log.warn(
          "{}",
          AuthEvent.builder(AuthEventType.REQUEST_RESET_PASSWORD, AuthEventOutcome.FAILURE)
              .email(request.email())
              .detail("User requested password reset multiple times")
              .build());
      throw new InvalidRequestException("You already requested a password reset code");
    }

    String verificationCode =
        verificationCodeService.generateAndSaveCode(user.getId(), VerificationType.PASSWORD_RESET);

    emailSendingService.sendVerificationEmail(
        user.getEmail(), "Password reset verification code", verificationCode);

    log.info(
        "{}",
        AuthEvent.builder(AuthEventType.REQUEST_RESET_PASSWORD, AuthEventOutcome.SUCCESS)
            .userId(user.getId())
            .email(user.getEmail())
            .detail("Succeeded request for password reset, code sent to email")
            .build());
  }

  @Override
  @Transactional
  public void confirmPasswordReset(PasswordResetConfirmRequest request) {
    log.info(
        "{}",
        AuthEvent.builder(AuthEventType.CONFIRM_PASSWORD_RESET, AuthEventOutcome.IN_PROGRESS)
            .email(request.email())
            .detail("Initialized confirm password reset")
            .build());

    User user =
        userRepository
            .findByEmail(request.email())
            .orElseThrow(
                () -> {
                  log.info(
                      "{}",
                      AuthEvent.builder(
                              AuthEventType.CONFIRM_PASSWORD_RESET, AuthEventOutcome.FAILURE)
                          .email(request.email())
                          .detail("User tried to confirm password reset with unverified account")
                          .build());
                  return new InvalidRequestException("Invalid email");
                });

    boolean isValid =
        verificationCodeService.isCodeValid(
            request.verificationCode(), user.getId(), VerificationType.PASSWORD_RESET);

    if (!isValid) {
      throw new InvalidRequestException("Invalid or expired reset code");
    }

    if (passwordEncoder.matches(request.newPassword(), user.getPasswordHash())) {
      log.info(
          "{}",
          AuthEvent.builder(AuthEventType.CONFIRM_PASSWORD_RESET, AuthEventOutcome.FAILURE)
              .email(request.email())
              .detail("User tried to reset password which is same as the old one")
              .build());
      throw new InvalidRequestException("New password can not be same as the old one");
    }

    passwordComplexityValidator.validate(request.newPassword());
    pwnedPasswordValidator.validatePasswordPwned(request.newPassword());

    user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
    userRepository.save(user);

    log.info(
        "{}",
        AuthEvent.builder(AuthEventType.CONFIRM_PASSWORD_RESET, AuthEventOutcome.SUCCESS)
            .email(request.email())
            .userId(user.getId())
            .detail("Succeeded confirm password reset")
            .build());
  }

  @Override
  public void resendAccountVerificationCode(ResendAccountVerificationRequest request) {
    log.info(
        "{}",
        AuthEvent.builder(AuthEventType.RESEND_ACCOUNT_VERIFICATION, AuthEventOutcome.IN_PROGRESS)
            .email(request.email())
            .detail("Initialized resend account verification code")
            .build());

    User user =
        userRepository
            .findByEmail(request.email())
            .orElseThrow(
                () -> {
                  log.info(
                      "{}",
                      AuthEvent.builder(
                              AuthEventType.RESEND_ACCOUNT_VERIFICATION, AuthEventOutcome.FAILURE)
                          .email(request.email())
                          .detail("User tried to resend verification code for nonexistent email")
                          .build());
                  return new InvalidRequestException("Invalid email");
                });

    if (user.isVerified()) {
      log.info(
          "{}",
          AuthEvent.builder(AuthEventType.RESEND_ACCOUNT_VERIFICATION, AuthEventOutcome.FAILURE)
              .email(request.email())
              .detail("User tried to resend verification code for a verified account")
              .build());
      throw new InvalidRequestException("Your account is verified");
    }

    if (verificationCodeService.isCodeSentAndValid(user.getId(), VerificationType.REGISTER)) {
      log.info(
          "{}",
          AuthEvent.builder(AuthEventType.RESEND_ACCOUNT_VERIFICATION, AuthEventOutcome.FAILURE)
              .email(request.email())
              .detail("User tried to resend verification code while a valid one exists")
              .build());
      throw new InvalidRequestException("Please check your email, a code was already sent");
    }

    String verificationCode =
        verificationCodeService.generateAndSaveCode(user.getId(), VerificationType.REGISTER);

    emailSendingService.sendVerificationEmail(
        user.getEmail(), "Email verification code", verificationCode);

    log.info(
        "{}",
        AuthEvent.builder(AuthEventType.RESEND_ACCOUNT_VERIFICATION, AuthEventOutcome.SUCCESS)
            .userId(user.getId())
            .build());
  }
}
