package com.atlantbh.cinemabh.service.impl;

import com.atlantbh.cinemabh.dto.internal.TokenClaims;
import com.atlantbh.cinemabh.entity.User;
import com.atlantbh.cinemabh.enums.AuthEventOutcome;
import com.atlantbh.cinemabh.enums.AuthEventType;
import com.atlantbh.cinemabh.logging.AuthEvent;
import com.atlantbh.cinemabh.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Instant;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {
  @Value("${application.security.jwt.secret-key}")
  private String secretKey;

  @Value("${application.security.jwt.expiration-min}")
  private long jwtExpirationMinutes;

  @Value("${application.security.jwt.refresh-token-expiration-min}")
  private long refreshExpirationMinutes;

  @Override
  public String generateJwt(User user) {
    return buildToken(user, AuthEventType.ISSUE_ACCESS_TOKEN, jwtExpirationMinutes);
  }

  @Override
  public String generateRefreshToken(User user) {
    return buildToken(user, AuthEventType.ISSUE_REFRESH_TOKEN, refreshExpirationMinutes);
  }

  private String buildToken(User user, AuthEventType eventType, long expirationMinutes) {
    log.info(
        "{}", AuthEvent.builder(eventType, AuthEventOutcome.SUCCESS).userId(user.getId()).build());

    return Jwts.builder()
        .claim("role", user.getUserRole().toString())
        .subject(user.getId().toString())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(Date.from(Instant.now().plusSeconds(expirationMinutes * 60)))
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }

  @Override
  public Long extractUserId(String token) {
    Claims claims = extractAllClaims(token);
    return Long.parseLong(claims.getSubject());
  }

  @Override
  public String extractRole(String token) {
    Claims claims = extractAllClaims(token);
    return claims.get("role", String.class);
  }

  @Override
  public boolean isTokenValid(String token) {
    try {
      Claims claims = extractAllClaims(token);
      return claims.getExpiration().after(new Date());
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean isTokenExpired(String token) {
    Claims claims = extractAllClaims(token);
    Date expiration = claims.getExpiration();
    return expiration.before(new Date()) && isTokenValid(token);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
  }

  @Override
  public TokenClaims parseToken(String token) {
    Claims claims = Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();

    return new TokenClaims(
        Long.parseLong(claims.getSubject()),
        claims.get("role", String.class),
        claims.getExpiration());
  }
}
