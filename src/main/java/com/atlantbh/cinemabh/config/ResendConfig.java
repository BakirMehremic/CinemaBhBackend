package com.atlantbh.cinemabh.config;

import com.resend.Resend;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResendConfig {
  private final String resendApiKey;

  public ResendConfig(@Value("${resend.api-key}") String resendApiKey) {
    if (resendApiKey == null || resendApiKey.isBlank()) {
      throw new IllegalArgumentException(
          "Resend API key must not be null or empty. Check application.properties or .env");
    }
    this.resendApiKey = resendApiKey;
  }

  @Bean
  public Resend resend() {
    return new Resend(resendApiKey);
  }
}
