package com.atlantbh.cinemabh.validator.email;


import com.atlantbh.cinemabh.config.properties.ValidationUrlProperties;
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
  private final RestTemplate restTemplate;
  private final ValidationUrlProperties validationUrlProperties;

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
      String response = restTemplate.getForObject(validationUrlProperties.getIana(), String.class);

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
