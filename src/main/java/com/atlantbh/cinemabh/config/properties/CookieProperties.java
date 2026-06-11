package com.atlantbh.cinemabh.config.properties;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app.cookies")
public class CookieProperties {
  @NotNull(message = "boolean secure is required")
  private Boolean secure;
}
