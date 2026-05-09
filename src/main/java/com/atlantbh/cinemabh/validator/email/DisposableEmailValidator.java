package com.atlantbh.cinemabh.validator.email;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
class DisposableEmailValidator {
  private static final String SOURCE_URL =
      "https://raw.githubusercontent.com/disposable-email-domains/disposable-email-domains/master/disposable_email_blocklist.conf";
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
      log.warn("Could not fetch disposable email records.{}", e.getMessage());
    }
  }
}
