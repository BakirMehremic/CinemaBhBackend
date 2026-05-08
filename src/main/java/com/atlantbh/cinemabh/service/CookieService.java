package com.atlantbh.cinemabh.service;

import com.atlantbh.cinemabh.dto.response.AuthResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface CookieService {
  void setTokenCookies(HttpServletResponse response, AuthResponse auth);

  String extractRefreshToken(HttpServletRequest request);
}
