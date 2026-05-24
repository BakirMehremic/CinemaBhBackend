package com.atlantbh.cinemabh.config.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Configuration
@Validated
@ConfigurationProperties(prefix = "application.security.jwt")
public class JwtProperties {

  @NotBlank(message = "JWT secret key is required")
  private String secretKey;

  @Positive(message = "JWT expiration must be positive")
  private long accessTokenExpirationMin;

  @Positive(message = "Refresh token expiration must be positive")
  private long refreshTokenExpirationMin;
}
