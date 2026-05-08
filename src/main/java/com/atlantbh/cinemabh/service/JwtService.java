package com.atlantbh.cinemabh.service;

import com.atlantbh.cinemabh.dto.response.AuthResponse;
import com.atlantbh.cinemabh.entity.User;
import jakarta.servlet.http.HttpServletResponse;

public interface JwtService {
  String generateJwt(User user);

  String generateRefreshToken(User user);

  String extractEmail(String token);

  String extractRole(String token);

  boolean isTokenValid(String token, String userEmail);

  void setTokenCookies(HttpServletResponse response, AuthResponse auth);
}
