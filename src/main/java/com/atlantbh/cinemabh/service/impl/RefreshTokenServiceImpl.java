package com.atlantbh.cinemabh.service.impl;

import static com.atlantbh.cinemabh.util.HashingUtils.toSha256;

import com.atlantbh.cinemabh.entity.RefreshToken;
import com.atlantbh.cinemabh.repository.RefreshTokenRepository;
import com.atlantbh.cinemabh.service.JwtService;
import com.atlantbh.cinemabh.service.RefreshTokenService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    tokenEntity.setTokenHash(toSha256(token));
    tokenEntity.setCreatedAt(LocalDateTime.now());

    refreshTokenRepository.save(tokenEntity);
  }

  @Override
  public boolean isRefreshTokenValid(String token) {
    if (jwtService.isTokenExpired(token)) {
      return false;
    }

    return refreshTokenRepository
        .findByUserId(jwtService.extractUserId(token))
        .map(
            storedToken -> {
              return toSha256(token).equals(storedToken.getTokenHash());
            })
        .orElse(false);
  }
}
