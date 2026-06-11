package com.atlantbh.cinemabh.config.properties;

import jakarta.validation.constraints.Positive;
import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app.rate-limit")
public class RateLimitProperties {
  @Positive(message = "requests per minute must be positive")
  private int requestsPerMinute;

  @DurationMin(seconds = 30)
  private Duration verificationResendCooldown;
}
