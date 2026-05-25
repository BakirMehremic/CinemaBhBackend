package com.atlantbh.cinemabh.config.properties;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@Configuration
@ConfigurationProperties(prefix = "verification")
public class VerificationProperties {
  @Positive(message = "Verification expiration minutes must be positive")
  private long expirationMinutes;

  @Positive(message = "Schedule delete delay must be positive")
  private long scheduleDeleteMs;
}
