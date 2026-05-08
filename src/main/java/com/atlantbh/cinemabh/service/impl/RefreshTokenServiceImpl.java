package com.atlantbh.cinemabh.service.impl;

import com.atlantbh.cinemabh.entity.RefreshToken;
import com.atlantbh.cinemabh.repository.RefreshTokenRepository;
import com.atlantbh.cinemabh.service.JwtService;
import com.atlantbh.cinemabh.service.RefreshTokenService;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
  private final RefreshTokenRepository refreshTokenRepository;
  private final JwtService jwtService;

  @Override
  public void hashAndSaveRefreshToken(String token) {
    Long userId = jwtService.extractUserId(token);

    RefreshToken tokenEntity =
        refreshTokenRepository.findByUserId(userId).orElse(new RefreshToken());

    tokenEntity.setUserId(userId);
    tokenEntity.setTokenHash(hashToken(token));
    tokenEntity.setCreatedAt(LocalDateTime.now());

    refreshTokenRepository.save(tokenEntity);
  }

  @Override
  public boolean isRefreshTokenValid(String token) {
    if (jwtService.isTokenExpired(token)) {
      log.warn("expired");
      return false;
    }

    return refreshTokenRepository
        .findByUserId(jwtService.extractUserId(token))
        .map(
            storedToken -> {
              return hashToken(token).equals(storedToken.getTokenHash());
            })
        .orElse(false);
  }

  private String hashToken(String token) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(hash);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("Error hashing token", e);
    }
  }
}
