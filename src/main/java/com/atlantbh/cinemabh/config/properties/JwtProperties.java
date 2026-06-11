package com.atlantbh.cinemabh.config.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "application.security.jwt")
public class JwtProperties {

  @NotBlank(message = "JWT secret key is required")
  @Size(min = 32)
  private String secretKey;

  @DurationMin(seconds = 10)
  private Duration accessTokenExpiration;

  @DurationMin(minutes = 1)
  private Duration refreshTokenExpiration;
}
