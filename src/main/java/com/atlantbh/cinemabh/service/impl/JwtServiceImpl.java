package com.atlantbh.cinemabh.service.impl;

import com.atlantbh.cinemabh.dto.response.AuthResponse;
import com.atlantbh.cinemabh.entity.User;
import com.atlantbh.cinemabh.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {
  @Value("${application.security.jwt.secret-key}")
  private String secretKey;

  @Value("${application.security.jwt.expiration-ms}")
  private long jwtExpirationMs;

  @Value("${application.security.jwt.refresh-token-expiration-ms}")
  private long refreshExpirationMs;

  // TODO add id to db for revocation
  @Override
  public String generateJwt(User user) {
    return Jwts.builder()
        .claim("role", user.getUserRole().toString())
        .subject(user.getEmail())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }

  // TODO add id to db for revocation
  @Override
  public String generateRefreshToken(User user) {
    return Jwts.builder()
        .subject(user.getEmail())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + refreshExpirationMs))
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
  }

  @Override
  public String extractEmail(String token) {
    Claims claims = extractAllClaims(token);
    return claims.getSubject();
  }

  @Override
  public String extractRole(String token) {
    Claims claims = extractAllClaims(token);
    return claims.get("role", String.class);
  }

  @Override
  public boolean isTokenValid(String token, String userEmail) {
    String email = extractEmail(token);
    return (email.equals(userEmail) && !isTokenExpired(token));
  }

  @Override
  public void setTokenCookies(HttpServletResponse response, AuthResponse auth) {
    ResponseCookie accessCookie =
        ResponseCookie.from("access_token", auth.accessToken())
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(jwtExpirationMs / 1000)
            .sameSite("Strict")
            .build();

    ResponseCookie refreshCookie =
        ResponseCookie.from("refresh_token", auth.refreshToken())
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(refreshExpirationMs / 1000)
            .sameSite("Strict")
            .build();

    response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
    response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
  }

  private boolean isTokenExpired(String token) {
    Claims claims = extractAllClaims(token);
    Date expiration = claims.getExpiration();
    return expiration.before(new Date());
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
  }
}
