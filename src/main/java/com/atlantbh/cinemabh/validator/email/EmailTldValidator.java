package com.atlantbh.cinemabh.validator.email;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
class EmailTldValidator {
  private static final String IANA_URL = "https://data.iana.org/TLD/tlds-alpha-by-domain.txt";
  private final RestTemplate restTemplate;
  private Set<String> tldCache;

  boolean isValidTld(String tld) {
    if (tldCache == null) {
      fetchAndCacheTlds();
    }

    return tldCache.contains(tld.toUpperCase());
  }

  private void fetchAndCacheTlds() {
    if (tldCache != null) return;

    try {
      String response = restTemplate.getForObject(IANA_URL, String.class);

      tldCache =
          Arrays.stream(response.split("\n"))
              .filter(line -> !line.isBlank() && !line.startsWith("#"))
              .map(String::trim)
              .map(String::toUpperCase)
              .collect(Collectors.toSet());
    } catch (Exception e) {
      log.warn("Could not fetch tld records.{}", e.getMessage());
    }
  }
}
