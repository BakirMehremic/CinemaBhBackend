package com.atlantbh.cinemabh.util;

import com.atlantbh.cinemabh.config.properties.RateLimitProperties;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class AuthUtils {
  private final RateLimitProperties rateLimitProperties;

  public Instant getResendAt() {
    return Instant.now().plus(rateLimitProperties.getVerificationResendCooldown());
  }

  public Instant getResendAt(Instant createdAt) {
    return createdAt.plus(rateLimitProperties.getVerificationResendCooldown());
  }
}
