package com.atlantbh.cinemabh.service.impl;

import com.atlantbh.cinemabh.dto.request.user.LoginRequest;
import com.atlantbh.cinemabh.dto.request.user.RegisterUserRequest;
import com.atlantbh.cinemabh.dto.request.user.ResetPasswordRequest;
import com.atlantbh.cinemabh.dto.request.user.VerificationRequest;
import com.atlantbh.cinemabh.dto.response.AuthResponse;
import com.atlantbh.cinemabh.dto.response.UserPreviewResponse;
import com.atlantbh.cinemabh.entity.City;
import com.atlantbh.cinemabh.entity.User;
import com.atlantbh.cinemabh.enums.VerificationType;
import com.atlantbh.cinemabh.exception.InvalidRequestException;
import com.atlantbh.cinemabh.exception.UnauthorizedException;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

  @Transactional
  @Override
  public UserPreviewResponse registerUser(RegisterUserRequest request) {
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
                throw new InvalidRequestException("Email already taken");
              }
              throw new InvalidRequestException("Phone number already taken");
            });

    City city =
        cityRepository
            .findById(request.cityId())
            .orElseThrow(() -> new InvalidRequestException("Nonexistent city id"));

    User user =
        userMapper.toEntity(
            request, phoneNumberNormalized, city, passwordEncoder.encode(request.password()));

    User savedUser = userRepository.save(user);

    String verificationCode =
        verificationCodeService.generateAndSaveCode(user.getId(), VerificationType.REGISTER);

    emailSendingService.sendVerificationEmail(
        user.getEmail(), "Email verification code", verificationCode);

    return userMapper.toPreviewResponse(savedUser);
  }

  @Transactional
  @Override
  public AuthResponse login(LoginRequest request) {
    User user =
        userRepository
            .findByEmail(request.email())
            .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

    if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
      throw new UnauthorizedException("Invalid email or password");
    }

    if (!user.isVerified()) {
      throw new UnauthorizedException("Please verify your account");
    }

    String jwtToken = jwtService.generateJwt(user);
    String refreshToken = jwtService.generateRefreshToken(user);

    refreshTokenService.hashAndSaveRefreshToken(refreshToken);

    return new AuthResponse(jwtToken, refreshToken, userMapper.toPreviewResponse(user));
  }

  @Transactional
  @Override
  public AuthResponse refresh(String refreshToken) {
    if (!refreshTokenService.isRefreshTokenValid(refreshToken)) {
      throw new UnauthorizedException("Invalid refresh token");
    }

    User user =
        userRepository
            .findById(jwtService.extractUserId(refreshToken))
            .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

    String jwtToken = jwtService.generateJwt(user);
    String refresh = jwtService.generateRefreshToken(user);

    refreshTokenService.hashAndSaveRefreshToken(refreshToken);

    return new AuthResponse(jwtToken, refresh, userMapper.toPreviewResponse(user));
  }

  @Transactional
  @Override
  public AuthResponse activateAccount(VerificationRequest request) {
    User user =
        userRepository
            .findByEmail(request.email())
            .orElseThrow(() -> new InvalidRequestException("Invalid email"));

    if (user.isVerified()) {
      throw new InvalidRequestException("Your account is already verified");
    }

    if (!verificationCodeService.isCodeValid(
        request.verificationCode(), user.getId(), VerificationType.REGISTER)) {
      throw new UnauthorizedException("Invalid verification code");
    }

    user.setVerified(true);
    userRepository.save(user);

    String jwtToken = jwtService.generateJwt(user);
    String refreshToken = jwtService.generateRefreshToken(user);

    refreshTokenService.hashAndSaveRefreshToken(refreshToken);

    return new AuthResponse(jwtToken, refreshToken, userMapper.toPreviewResponse(user));
  }

  @Override
  public void requestPasswordReset(ResetPasswordRequest request) {
    User user =
        userRepository
            .findByEmail(request.email())
            .orElseThrow(() -> new InvalidRequestException("Invalid email"));

    if (!user.isVerified()) {
      throw new UnauthorizedException("Please verify your account");
    }

    String verificationCode =
        verificationCodeService.generateAndSaveCode(user.getId(), VerificationType.PASSWORD_RESET);

    emailSendingService.sendVerificationEmail(
        user.getEmail(), "Password reset verification code", verificationCode);
  }
}
