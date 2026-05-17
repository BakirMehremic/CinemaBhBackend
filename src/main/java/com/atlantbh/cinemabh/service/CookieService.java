package com.atlantbh.cinemabh.service;

import com.atlantbh.cinemabh.dto.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;

public interface CookieService {
  void setTokenCookies(HttpServletResponse response, AuthResponse auth);

  Optional<String> extractRefreshToken(HttpServletRequest request);

  Optional<String> extractAccessToken(HttpServletRequest request);

  Long extractUserId(HttpServletRequest request);
}
