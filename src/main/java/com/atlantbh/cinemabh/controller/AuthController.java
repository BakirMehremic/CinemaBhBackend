package com.atlantbh.cinemabh.controller;

import com.atlantbh.cinemabh.dto.request.user.*;
import com.atlantbh.cinemabh.dto.response.*;
import com.atlantbh.cinemabh.service.AuthService;
import com.atlantbh.cinemabh.service.CookieService;
import com.atlantbh.cinemabh.util.AuthUtils;
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
@RequestMapping("/auth")
public class AuthController {
  private final AuthService authService;
  private final CookieService cookieService;
  private final AuthUtils authUtils;

  @PostMapping("/register")
  public ResponseEntity<ResendAtResponse<MessageDataResponse<UserDetailsResponse>>> register(
      @Valid @RequestBody RegisterUserRequest request) {
    UserDetailsResponse createdUser = authService.registerUser(request);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            new ResendAtResponse<>(
                new MessageDataResponse<>(
                    "Registration successful, please verify your account via email.", createdUser),
                authUtils.getResendAt()));
  }

  @PostMapping("/login")
  public ResponseEntity<UserDetailsResponse> login(
      @Valid @RequestBody LoginRequest request, HttpServletResponse response) {

    AuthResponse authResponse = authService.login(request);
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

    AuthResponse authResponse = authService.refresh(refreshToken.get());

    cookieService.setTokenCookies(response, authResponse);
    return ResponseEntity.ok(authResponse.user());
  }

  @PostMapping("/verify")
  public ResponseEntity<UserDetailsResponse> verifyAccount(
      HttpServletResponse response, @Valid @RequestBody VerificationRequest request) {

    AuthResponse authResponse = authService.verifyAccount(request);
    cookieService.setTokenCookies(response, authResponse);
    return ResponseEntity.ok(authResponse.user());
  }

  @PostMapping("/password/reset/request")
  public ResponseEntity<MessageResponse> requestPasswordReset(
      @Valid @RequestBody PasswordResetRequest request) {

    authService.requestPasswordReset(request);
    return ResponseEntity.status(HttpStatus.ACCEPTED)
        .body(new MessageResponse("Reset code sent to your email."));
  }

  @PostMapping("/password/reset/verify")
  public ResponseEntity<MessageResponse> confirmPasswordReset(
      @Valid @RequestBody PasswordResetConfirmRequest request) {
    authService.confirmPasswordReset(request);

    return ResponseEntity.ok(
        new MessageResponse("Password has been reset successfully, you can now log in."));
  }

  @PostMapping("/verify/resend")
  public ResponseEntity<ResendAtResponse<MessageResponse>> resendAccountVerificationCode(
      @Valid @RequestBody ResendAccountVerificationRequest request) {
    authService.resendAccountVerificationCode(request);

    return ResponseEntity.ok(
        new ResendAtResponse<>(
            new MessageResponse("A new verification code was sent to your email."),
            authUtils.getResendAt()));
  }

  @PostMapping("/logout")
  public ResponseEntity<MessageResponse> logout(HttpServletResponse response) {
    cookieService.clearTokenCookies(response);

    return ResponseEntity.ok(new MessageResponse("Logout successful."));
  }
}
