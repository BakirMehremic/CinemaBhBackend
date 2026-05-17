package com.atlantbh.cinemabh.service.impl;

import static com.atlantbh.cinemabh.constant.AuthConstants.ACCESS_TOKEN_NAME;
import static com.atlantbh.cinemabh.constant.AuthConstants.REFRESH_TOKEN_NAME;

import com.atlantbh.cinemabh.dto.response.AuthResponse;
import com.atlantbh.cinemabh.service.CookieService;
import com.atlantbh.cinemabh.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
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

  @Value("${app.cookies.secure}")
  private boolean secureCookies;

  @Override
  public void setTokenCookies(HttpServletResponse response, AuthResponse auth) {
    ResponseCookie accessCookie =
        ResponseCookie.from(ACCESS_TOKEN_NAME, auth.accessToken())
            .httpOnly(true)
            .secure(secureCookies)
            .path("/")
            .maxAge(jwtExpirationMinutes * 60)
            .sameSite("Strict")
            .build();

    ResponseCookie refreshCookie =
        ResponseCookie.from(REFRESH_TOKEN_NAME, auth.refreshToken())
            .httpOnly(true)
            .secure(secureCookies)
            .path("/")
            .maxAge(refreshExpirationMinutes * 60)
            .sameSite("Strict")
            .build();

    response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
    response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
  }

  @Override
  public Optional<String> extractRefreshToken(HttpServletRequest request) {
    String refreshToken = getCookieValue(request, REFRESH_TOKEN_NAME);
    if (refreshToken != null) return Optional.of(refreshToken);
    return Optional.empty();
  }

  @Override
  public Optional<String> extractAccessToken(HttpServletRequest request) {
    String accessToken = getCookieValue(request, ACCESS_TOKEN_NAME);
    if (accessToken != null) return Optional.of(accessToken);
    return Optional.empty();
  }

  @Override
  public Long extractUserId(HttpServletRequest request) {
    String token = getCookieValue(request, ACCESS_TOKEN_NAME);

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
