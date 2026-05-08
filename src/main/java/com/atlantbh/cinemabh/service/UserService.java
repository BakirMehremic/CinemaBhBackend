package com.atlantbh.cinemabh.service;

import com.atlantbh.cinemabh.dto.request.user.LoginRequest;
import com.atlantbh.cinemabh.dto.request.user.RegisterUserRequest;
import com.atlantbh.cinemabh.dto.response.AuthResponse;

public interface UserService {
  AuthResponse registerUser(RegisterUserRequest request);

  AuthResponse login(LoginRequest request);
}
