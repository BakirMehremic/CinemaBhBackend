package com.atlantbh.cinemabh.util;

import static com.atlantbh.cinemabh.constant.RateLimitConstants.VERIFICATION_CODES_RESEND_LIMIT_SECONDS;

import java.time.Instant;

public final class AuthUtils {
  public static Instant getResendAt() {
    return Instant.now().plusSeconds(VERIFICATION_CODES_RESEND_LIMIT_SECONDS);
  }

  public static Instant getResendAt(Instant createdAt) {
    return createdAt.plusSeconds(VERIFICATION_CODES_RESEND_LIMIT_SECONDS);
  }
}
