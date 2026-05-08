package com.atlantbh.cinemabh.validator.email;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
class DisposableEmailValidator {
  private static final String SOURCE_URL =
      "https://raw.githubusercontent.com/disposable-email-domains/disposable-email-domains/master/disposable_email_blocklist.conf";
  private final Logger logger = LoggerFactory.getLogger(DisposableEmailValidator.class);
  private final Set<String> disposableDomains = new HashSet<>();
  private final RestTemplate restTemplate;

  public DisposableEmailValidator(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
    loadDomains();
  }

  boolean isDisposable(String domain) {
    return disposableDomains.contains(domain.toLowerCase());
  }

  private void loadDomains() {
    try {
      String response = restTemplate.getForObject(SOURCE_URL, String.class);

      Arrays.stream(response.split("\n"))
          .map(String::trim)
          .filter(line -> !line.isEmpty() && !line.startsWith("#"))
          .map(String::toLowerCase)
          .forEach(disposableDomains::add);
    } catch (Exception e) {
      logger.warn("Could not fetch disposable email records.{}", e.getMessage());
    }
  }
}
