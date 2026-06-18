package com.atlantbh.cinemabh.config.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "application.security.validation.urls")
public class ValidationUrlProperties {
  @NotBlank(message = "IANA URL is required")
  @URL(message = "IANA URL must be a valid URL format")
  private String iana;

  @NotBlank(message = "Disposable email domains URL is required")
  @URL(message = "Disposable email URL must be a valid URL format")
  private String disposableEmail;

  @NotBlank(message = "Pwned API URL is required")
  @URL(message = "Pwned API URL must be a valid URL format")
  private String pwnedApi;
}
