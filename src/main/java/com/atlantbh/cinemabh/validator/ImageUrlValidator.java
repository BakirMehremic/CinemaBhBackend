package com.atlantbh.cinemabh.validator;

import com.atlantbh.cinemabh.enums.AuthEventOutcome;
import com.atlantbh.cinemabh.enums.AuthEventType;
import com.atlantbh.cinemabh.exception.InvalidRequestException;
import com.atlantbh.cinemabh.logging.AuthEvent;
import java.net.URI;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
      String host = uri.getHost().toLowerCase();

      Pattern internalIpPattern =
          Pattern.compile("^(localhost|127\\.|10\\.|192\\.168\\.|169\\.254\\.)");

      if (internalIpPattern.matcher(host).find()) {
        log.warn("Blocked potential SSRF attack to internal host: {}", host);
        throw new InvalidRequestException("Access to internal addresses is forbidden.");
      }

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

      if (contentLength == 0) {
        log.warn(
            "{}",
            AuthEvent.builder(AuthEventType.IMAGE_VALIDATION, AuthEventOutcome.FAILURE)
                .detail("Submitted empty image file")
                .build());
        throw new InvalidRequestException("Image file must not be empty");
      }

      if (headers.getContentLength() > MAX_IMAGE_SIZE) {
        log.warn(
            "{}",
            AuthEvent.builder(AuthEventType.IMAGE_VALIDATION, AuthEventOutcome.FAILURE)
                .detail("Too large image submitted")
                .build());
        throw new InvalidRequestException("Image too large");
      }

    } catch (HttpClientErrorException e) {
      HttpStatusCode status = e.getStatusCode();

      log.warn(
          "{}",
          AuthEvent.builder(AuthEventType.IMAGE_VALIDATION, AuthEventOutcome.FAILURE)
              .detail(e.getMessage())
              .build());

      if (status == HttpStatus.NOT_FOUND) {
        throw new InvalidRequestException("Image url not found");
      }

      if (status == HttpStatus.FORBIDDEN || status == HttpStatus.UNAUTHORIZED) {
        throw new InvalidRequestException("Image URL is not publicly accessible");
      }

      throw new InvalidRequestException("Image URL returned HTTP " + status.value());
    } catch (Exception e) {
      log.error("URL validation failed for {}: {}", url, e.getMessage());
      throw new InvalidRequestException("Image URL could not be validated");
    }
  }
}
