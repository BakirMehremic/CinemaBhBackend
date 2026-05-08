package com.atlantbh.cinemabh.service;

public interface RefreshTokenService {
  void hashAndSaveRefreshToken(String token);

  boolean isRefreshTokenValid(String token);
}
