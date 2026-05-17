package com.atlantbh.cinemabh.validator.email;

import static com.atlantbh.cinemabh.constant.AuthConstants.DISPOSABLE_EMAIL_DOMAINS_URL;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
class DisposableEmailValidator {
  private static final Pattern DOMAIN_PATTERN =
      Pattern.compile("^(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$");
  private final Set<String> disposableDomains = new HashSet<>();
  private final RestTemplate restTemplate;

  public DisposableEmailValidator(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
    loadDomains();
  }

  boolean isDisposable(String domain) {
    return disposableDomains.contains(domain.toLowerCase(Locale.ROOT));
  }

  private void loadDomains() {
    try {
      String response = restTemplate.getForObject(DISPOSABLE_EMAIL_DOMAINS_URL, String.class);

      Arrays.stream(response.split("\n"))
          .map(String::trim)
          .filter(line -> !line.isEmpty() && !line.startsWith("#"))
          .map(String::toLowerCase)
          .filter(line -> DOMAIN_PATTERN.matcher(line).matches())
          .forEach(disposableDomains::add);
    } catch (Exception e) {
      log.error("Could not fetch disposable email records.{}", e.getMessage());
      throw new IllegalStateException("Failed to initialize disposable email validator", e);
    }
  }
}
