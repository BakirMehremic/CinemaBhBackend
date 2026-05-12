package com.atlantbh.cinemabh.service.impl;

import com.atlantbh.cinemabh.dto.request.user.*;
import com.atlantbh.cinemabh.dto.response.AuthResponse;
import com.atlantbh.cinemabh.dto.response.UserPreviewResponse;
import com.atlantbh.cinemabh.entity.City;
import com.atlantbh.cinemabh.entity.User;
import com.atlantbh.cinemabh.enums.AuthEventOutcome;
import com.atlantbh.cinemabh.enums.AuthEventType;
import com.atlantbh.cinemabh.enums.VerificationType;
import com.atlantbh.cinemabh.exception.InvalidRequestException;
import com.atlantbh.cinemabh.exception.UnauthorizedException;
import com.atlantbh.cinemabh.logging.AuthEvent;
import com.atlantbh.cinemabh.mapper.UserMapper;
import com.atlantbh.cinemabh.repository.CityRepository;
import com.atlantbh.cinemabh.repository.UserRepository;
import com.atlantbh.cinemabh.service.*;
import com.atlantbh.cinemabh.validator.ImageUrlValidator;
import com.atlantbh.cinemabh.validator.PhoneNumberValidator;
import com.atlantbh.cinemabh.validator.ReservedNameValidator;
import com.atlantbh.cinemabh.validator.password.PasswordComplexityValidator;
import com.atlantbh.cinemabh.validator.password.PwnedPasswordValidator;
import java.awt.*;
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

  @Override
  @Transactional
  public UserPreviewResponse registerUser(RegisterUserRequest request) {
    log.info(
        "{}",
        AuthEvent.builder(AuthEventType.REGISTER, AuthEventOutcome.IN_PROGRESS)
            .email(request.email())
            .detail("Initialized register")
            .build());

    passwordComplexityValidator.validate(request.password());
    ReservedNameValidator.validate(request.firstName(), request.lastName());
    pwnedPasswordValidator.validatePasswordPwned(request.password());
    imageUrlValidator.validateImageUrl(request.imageUrl());
    String phoneNumberNormalized = phoneNumberValidator.validateAndNormalize(request.phoneNumber());

    userRepository
        .findByEmailOrPhoneNumber(request.email(), phoneNumberNormalized)
        .ifPresent(
            user -> {
              if (user.getEmail().equals(request.email())) {
                log.info(
                    "{}",
                    AuthEvent.builder(AuthEventType.REGISTER, AuthEventOutcome.FAILURE)
                        .email(request.email())
                        .detail("User submitted an email which is in use")
                        .build());
                throw new InvalidRequestException("Email already taken");
              }
              log.info(
                  "{}",
                  AuthEvent.builder(AuthEventType.REGISTER, AuthEventOutcome.FAILURE)
                      .phoneNumber(request.phoneNumber())
                      .detail("User submitted a phone number which is in use")
                      .build());
              throw new InvalidRequestException("Phone number already taken");
            });

    City city =
        cityRepository
            .findById(request.cityId())
            .orElseThrow(
                () -> {
                  log.warn(
                      "{}",
                      AuthEvent.builder(AuthEventType.REGISTER, AuthEventOutcome.FAILURE)
                          .detail("User submitted nonexistent city id: " + request.cityId())
                          .build());

                  return new InvalidRequestException("Nonexistent city id");
                });

    User user =
        userMapper.toEntity(
            request, phoneNumberNormalized, city, passwordEncoder.encode(request.password()));

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

    return userMapper.toPreviewResponse(savedUser);
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
      throw new UnauthorizedException("Please verify your account");
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

    return new AuthResponse(jwtToken, refreshToken, userMapper.toPreviewResponse(user));
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

    return new AuthResponse(jwtToken, refresh, userMapper.toPreviewResponse(user));
  }

  @Override
  @Transactional
  public AuthResponse activateAccount(VerificationRequest request) {
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

    return new AuthResponse(jwtToken, refreshToken, userMapper.toPreviewResponse(user));
  }

  @Override
  @Transactional
  public void requestPasswordReset(ResetPasswordRequest request) {
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

    String verificationCode =
        verificationCodeService.generateAndSaveCode(user.getId(), VerificationType.PASSWORD_RESET);

    log.info(
        "{}",
        AuthEvent.builder(AuthEventType.REQUEST_RESET_PASSWORD, AuthEventOutcome.SUCCESS)
            .userId(user.getId())
            .email(user.getEmail())
            .detail("Succeeded request for password reset, code sent to email")
            .build());

    emailSendingService.sendVerificationEmail(
        user.getEmail(), "Password reset verification code", verificationCode);
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
            request.code(), user.getId(), VerificationType.PASSWORD_RESET);

    if (!isValid) {
      throw new InvalidRequestException("Invalid or expired reset code");
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
}
