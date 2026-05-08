package com.atlantbh.cinemabh.validator;

import com.atlantbh.cinemabh.exception.InvalidRequestException;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class ImageUrlValidator {
  private static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024;
  private final RestTemplate restTemplate;
  private final Logger logger = LoggerFactory.getLogger(ImageUrlValidator.class);

  public void validateImageUrl(String url) {
    try {
      URI uri = URI.create(url);

      HttpHeaders headers = restTemplate.headForHeaders(uri);

      MediaType contentType = headers.getContentType();
      if (contentType != null && !contentType.getType().equals("image")) {
        throw new InvalidRequestException("Invalid image media type");
      }

      long contentLength = headers.getContentLength();

      if (headers.getContentLength() > MAX_IMAGE_SIZE) {
        throw new InvalidRequestException("Image too large");
      }

      if (contentLength == 0) {
        throw new InvalidRequestException("Image file must not be empty");
      }

    } catch (HttpClientErrorException.NotFound e) {
      throw new InvalidRequestException("Image url not found");
    } catch (Exception e) {
      logger.error("URL validation failed for {}: {}", url, e.getMessage());
    }
  }
}
