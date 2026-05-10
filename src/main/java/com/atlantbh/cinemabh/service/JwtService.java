package com.atlantbh.cinemabh.service;

import com.atlantbh.cinemabh.entity.User;

public interface JwtService {
  String generateJwt(User user);

  String generateRefreshToken(User user);

  Long extractUserId(String token);

  String extractRole(String token);

  boolean isTokenValid(String token);

  boolean isTokenExpired(String token);
}
