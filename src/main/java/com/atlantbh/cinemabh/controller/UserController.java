package com.atlantbh.cinemabh.controller;

import com.atlantbh.cinemabh.dto.request.user.LoginRequest;
import com.atlantbh.cinemabh.dto.request.user.RegisterUserRequest;
import com.atlantbh.cinemabh.dto.response.AuthResponse;
import com.atlantbh.cinemabh.dto.response.UserPreviewResponse;
import com.atlantbh.cinemabh.service.CookieService;
import com.atlantbh.cinemabh.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
  private final UserService userService;
  private final CookieService cookieService;

  @PostMapping("/register")
  public ResponseEntity<UserPreviewResponse> register(
      @Valid @RequestBody RegisterUserRequest request, HttpServletResponse response) {
    AuthResponse authResponse = userService.registerUser(request);
    cookieService.setTokenCookies(response, authResponse);
    return ResponseEntity.ok(authResponse.user());
  }

  @PostMapping("/login")
  public ResponseEntity<UserPreviewResponse> login(
      @Valid @RequestBody LoginRequest request, HttpServletResponse response) {
    AuthResponse authResponse = userService.login(request);
    cookieService.setTokenCookies(response, authResponse);
    return ResponseEntity.ok(authResponse.user());
  }

  @PostMapping("/refresh")
  public ResponseEntity<UserPreviewResponse> refresh(
      HttpServletRequest request, HttpServletResponse response) {
    AuthResponse authResponse = userService.refresh(cookieService.extractRefreshToken(request));
    cookieService.setTokenCookies(response, authResponse);
    return ResponseEntity.ok(authResponse.user());
  }
}
