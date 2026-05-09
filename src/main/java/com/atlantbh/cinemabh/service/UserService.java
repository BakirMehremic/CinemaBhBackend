package com.atlantbh.cinemabh.service;

import com.atlantbh.cinemabh.dto.request.user.LoginRequest;
import com.atlantbh.cinemabh.dto.request.user.RegisterUserRequest;
import com.atlantbh.cinemabh.dto.request.user.ResetPasswordRequest;
import com.atlantbh.cinemabh.dto.request.user.VerificationRequest;
import com.atlantbh.cinemabh.dto.response.AuthResponse;
import com.atlantbh.cinemabh.dto.response.UserPreviewResponse;

public interface UserService {
  UserPreviewResponse registerUser(RegisterUserRequest request);

  AuthResponse login(LoginRequest request);

  AuthResponse refresh(String refreshToken);

  AuthResponse activateAccount(VerificationRequest request);

  void requestPasswordReset(ResetPasswordRequest request);
}
