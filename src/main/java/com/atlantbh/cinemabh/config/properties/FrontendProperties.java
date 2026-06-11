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
@ConfigurationProperties(prefix = "frontend")
public class FrontendProperties {

  @NotBlank(message = "Frontend URL is required")
  @URL(message = "Frontend URL must be a valid URL format")
  private String url;
}
