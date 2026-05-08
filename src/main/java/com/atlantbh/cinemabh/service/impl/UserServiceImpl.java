package com.atlantbh.cinemabh.service.impl;

import com.atlantbh.cinemabh.dto.request.user.LoginRequest;
import com.atlantbh.cinemabh.dto.request.user.RegisterUserRequest;
import com.atlantbh.cinemabh.dto.response.AuthResponse;
import com.atlantbh.cinemabh.entity.City;
import com.atlantbh.cinemabh.entity.User;
import com.atlantbh.cinemabh.exception.InvalidRequestException;
import com.atlantbh.cinemabh.exception.UnauthorizedException;
import com.atlantbh.cinemabh.mapper.UserMapper;
import com.atlantbh.cinemabh.repository.CityRepository;
import com.atlantbh.cinemabh.repository.UserRepository;
import com.atlantbh.cinemabh.service.JwtService;
import com.atlantbh.cinemabh.service.UserService;
import com.atlantbh.cinemabh.validator.ImageUrlValidator;
import com.atlantbh.cinemabh.validator.PhoneNumberValidator;
import com.atlantbh.cinemabh.validator.ReservedNameValidator;
import com.atlantbh.cinemabh.validator.password.PasswordComplexityValidator;
import com.atlantbh.cinemabh.validator.password.PwnedPasswordValidator;
import java.awt.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

  // TODO user verification code ?
  @Override
  public AuthResponse registerUser(RegisterUserRequest request) {
    passwordComplexityValidator.validate(request.password());
    phoneNumberValidator.validate(request.phoneNumber());
    ReservedNameValidator.validate(request.firstName(), request.lastName());
    pwnedPasswordValidator.validatePasswordPwned(request.password());
    imageUrlValidator.validateImageUrl(request.imageUrl());

    userRepository
        .findByEmailOrPhoneNumber(request.email(), request.phoneNumber())
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

    User user = userMapper.toEntity(request, city, passwordEncoder.encode(request.password()));

    User savedUser = userRepository.save(user);

    String jwtToken = jwtService.generateJwt(savedUser);
    String refreshToken = jwtService.generateRefreshToken(savedUser);

    return new AuthResponse(jwtToken, refreshToken, userMapper.toPreviewResponse(savedUser));
  }

  @Override
  public AuthResponse login(LoginRequest request) {
    User user =
        userRepository
            .findByEmail(request.email())
            .orElseThrow(() -> new UnauthorizedException("Invalid email or password"));

    if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
      throw new UnauthorizedException("Invalid email or password");
    }

    String jwtToken = jwtService.generateJwt(user);
    String refreshToken = jwtService.generateRefreshToken(user);

    return new AuthResponse(jwtToken, refreshToken, userMapper.toPreviewResponse(user));
  }
}
