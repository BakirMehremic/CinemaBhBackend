package com.atlantbh.cinemabh.config.properties;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "resend")
public class ResendProperties {
  @NotBlank(message = "Resend API key is required")
  private String apiKey;

  @NotBlank(message = "Sender email is required")
  @Email(message = "From email must be a valid email format")
  private String fromEmail;
}
