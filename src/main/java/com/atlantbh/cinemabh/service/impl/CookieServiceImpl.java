package com.atlantbh.cinemabh.service.impl;

import com.atlantbh.cinemabh.dto.response.AuthResponse;
import com.atlantbh.cinemabh.service.CookieService;
import com.atlantbh.cinemabh.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CookieServiceImpl implements CookieService {
  private final JwtService jwtService;

  @Value("${application.security.jwt.expiration-min}")
  private long jwtExpirationMinutes;

  @Value("${application.security.jwt.refresh-token-expiration-min}")
  private long refreshExpirationMinutes;

  @Override
  public void setTokenCookies(HttpServletResponse response, AuthResponse auth) {
    ResponseCookie accessCookie =
        ResponseCookie.from("access_token", auth.accessToken())
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(jwtExpirationMinutes * 60)
            .sameSite("Strict")
            .build();

    ResponseCookie refreshCookie =
        ResponseCookie.from("refresh_token", auth.refreshToken())
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(refreshExpirationMinutes * 60)
            .sameSite("Strict")
            .build();

    response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
    response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
  }

  @Override
  public String extractRefreshToken(HttpServletRequest request) {
    return getCookieValue(request, "refresh_token");
  }

  @Override
  public String extractAccessToken(HttpServletRequest request) {
    return getCookieValue(request, "access_token");
  }

  @Override
  public Long extractUserId(HttpServletRequest request) {
    String token = getCookieValue(request, "access_token");

    return jwtService.extractUserId(token);
  }

  private String getCookieValue(HttpServletRequest request, String name) {
    if (request.getCookies() == null) {
      return null;
    }

    return Arrays.stream(request.getCookies())
        .filter(cookie -> name.equals(cookie.getName()))
        .map(Cookie::getValue)
        .findFirst()
        .orElse(null);
  }
}
