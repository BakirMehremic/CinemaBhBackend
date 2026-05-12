package com.atlantbh.cinemabh.validator;

import com.atlantbh.cinemabh.enums.AuthEventOutcome;
import com.atlantbh.cinemabh.enums.AuthEventType;
import com.atlantbh.cinemabh.exception.InvalidRequestException;
import com.atlantbh.cinemabh.logging.AuthEvent;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageUrlValidator {
  private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024;
  private final RestTemplate restTemplate;

  public void validateImageUrl(String url) {
    try {
      URI uri = URI.create(url);

      HttpHeaders headers = restTemplate.headForHeaders(uri);

      MediaType contentType = headers.getContentType();
      if (contentType != null && !contentType.getType().equals("image")) {
        log.warn(
            "{}",
            AuthEvent.builder(AuthEventType.IMAGE_VALIDATION, AuthEventOutcome.FAILURE)
                .detail("Invalid media type for image submitted")
                .build());
        throw new InvalidRequestException("Invalid image media type");
      }

      long contentLength = headers.getContentLength();

      if (headers.getContentLength() > MAX_IMAGE_SIZE) {
        log.warn(
            "{}",
            AuthEvent.builder(AuthEventType.IMAGE_VALIDATION, AuthEventOutcome.FAILURE)
                .detail("Too large image submitted")
                .build());
        throw new InvalidRequestException("Image too large");
      }

      if (contentLength == 0) {
        log.warn(
            "{}",
            AuthEvent.builder(AuthEventType.IMAGE_VALIDATION, AuthEventOutcome.FAILURE)
                .detail("Submitted empty image file")
                .build());
        throw new InvalidRequestException("Image file must not be empty");
      }

    } catch (HttpClientErrorException.NotFound e) {
      log.warn(
          "{}",
          AuthEvent.builder(AuthEventType.IMAGE_VALIDATION, AuthEventOutcome.FAILURE)
              .detail("Image url not found")
              .build());
      throw new InvalidRequestException("Image url not found");
    } catch (Exception e) {
      log.error("URL validation failed for {}: {}", url, e.getMessage());
    }
  }
}
