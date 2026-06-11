package com.atlantbh.cinemabh.service.impl;

import static com.atlantbh.cinemabh.util.HashingUtils.toSha256;

import com.atlantbh.cinemabh.config.properties.JwtProperties;
import com.atlantbh.cinemabh.entity.RefreshToken;
import com.atlantbh.cinemabh.entity.User;
import com.atlantbh.cinemabh.repository.RefreshTokenRepository;
import com.atlantbh.cinemabh.repository.UserRepository;
import com.atlantbh.cinemabh.service.JwtService;
import com.atlantbh.cinemabh.service.RefreshTokenService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
  private final RefreshTokenRepository refreshTokenRepository;
  private final JwtService jwtService;
  private final UserRepository userRepository;
  private final JwtProperties jwtProperties;

  @Override
  @Transactional
  public void hashAndSaveRefreshToken(String token) {
    Long userId = jwtService.extractUserId(token);

    RefreshToken tokenEntity =
        refreshTokenRepository.findByUserId(userId).orElse(new RefreshToken());

    User user = userRepository.getReferenceById(userId);
    tokenEntity.setUser(user);
    tokenEntity.setTokenHash(toSha256(token));
    tokenEntity.setCreatedAt(LocalDateTime.now());
    tokenEntity.setExpiresAt(LocalDateTime.now().plus(jwtProperties.getRefreshTokenExpiration()));

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
