package com.atlantbh.cinemabh.controller;

import com.atlantbh.cinemabh.dto.request.user.*;
import com.atlantbh.cinemabh.dto.response.AuthResponse;
import com.atlantbh.cinemabh.dto.response.MessageDataResponse;
import com.atlantbh.cinemabh.dto.response.MessageResponse;
import com.atlantbh.cinemabh.dto.response.UserDetailsResponse;
import com.atlantbh.cinemabh.service.CookieService;
import com.atlantbh.cinemabh.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
  private final UserService userService;
  private final CookieService cookieService;

  @PostMapping("/register")
  public ResponseEntity<MessageDataResponse<UserDetailsResponse>> register(
      @Valid @RequestBody RegisterUserRequest request) {
    UserDetailsResponse createdUser = userService.registerUser(request);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            new MessageDataResponse<>(
                "Registration successful, please verify your account via email.", createdUser));
  }

  @PostMapping("/login")
  public ResponseEntity<UserDetailsResponse> login(
      @Valid @RequestBody LoginRequest request, HttpServletResponse response) {

    AuthResponse authResponse = userService.login(request);
    cookieService.setTokenCookies(response, authResponse);
    return ResponseEntity.ok(authResponse.user());
  }

  @PostMapping("/refresh")
  public ResponseEntity<UserDetailsResponse> refresh(
      HttpServletRequest request, HttpServletResponse response) {
    Optional<String> refreshToken = cookieService.extractRefreshToken(request);

    if (refreshToken.isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    AuthResponse authResponse = userService.refresh(refreshToken.get());

    cookieService.setTokenCookies(response, authResponse);
    return ResponseEntity.ok(authResponse.user());
  }

  @PostMapping("/verify")
  public ResponseEntity<UserDetailsResponse> verifyAccount(
      HttpServletResponse response, @Valid @RequestBody VerificationRequest request) {

    AuthResponse authResponse = userService.verifyAccount(request);
    cookieService.setTokenCookies(response, authResponse);
    return ResponseEntity.ok(authResponse.user());
  }

  @PostMapping("/password/reset/request")
  public ResponseEntity<MessageResponse> requestPasswordReset(
      @Valid @RequestBody PasswordResetRequest request) {

    userService.requestPasswordReset(request);
    return ResponseEntity.status(HttpStatus.ACCEPTED)
        .body(new MessageResponse("Reset code sent to your email."));
  }

  @PostMapping("/password/reset/verify")
  public ResponseEntity<MessageResponse> confirmPasswordReset(
      @Valid @RequestBody PasswordResetConfirmRequest request) {
    userService.confirmPasswordReset(request);

    return ResponseEntity.ok(
        new MessageResponse("Password has been reset successfully, you can now log in."));
  }
}
