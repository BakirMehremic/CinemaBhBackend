package com.atlantbh.cinemabh.exception;

import java.time.Instant;

public class UserNotVerifiedException extends RuntimeException {
  private final Instant resendVerificationCodeAt;

  public UserNotVerifiedException(String message, Instant resendVerificationCodeAt) {
    super(message);
    this.resendVerificationCodeAt = resendVerificationCodeAt;
  }

  public Instant getResendVerificationCodeAt() {
    return resendVerificationCodeAt;
  }
}
