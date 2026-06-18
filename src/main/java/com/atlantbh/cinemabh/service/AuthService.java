package com.atlantbh.cinemabh.service;

import com.atlantbh.cinemabh.dto.request.user.*;
import com.atlantbh.cinemabh.dto.response.AuthResponse;
import com.atlantbh.cinemabh.dto.response.UserDetailsResponse;

public interface AuthService {
  UserDetailsResponse registerUser(RegisterUserRequest request);

  AuthResponse login(LoginRequest request);

  AuthResponse refresh(String refreshToken);

  AuthResponse verifyAccount(VerificationRequest request);

  void requestPasswordReset(PasswordResetRequest request);

  void confirmPasswordReset(PasswordResetConfirmRequest request);

  void resendAccountVerificationCode(ResendAccountVerificationRequest request);
}
