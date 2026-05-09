package com.atlantbh.cinemabh.service;

import com.atlantbh.cinemabh.dto.request.user.*;
import com.atlantbh.cinemabh.dto.response.AuthResponse;
import com.atlantbh.cinemabh.dto.response.UserPreviewResponse;

public interface UserService {
  UserPreviewResponse registerUser(RegisterUserRequest request);

  AuthResponse login(LoginRequest request);

  AuthResponse refresh(String refreshToken);

  AuthResponse activateAccount(VerificationRequest request);

  void requestPasswordReset(ResetPasswordRequest request);

  void confirmPasswordReset(PasswordResetConfirmRequest request);
}
