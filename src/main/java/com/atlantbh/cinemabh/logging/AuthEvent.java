package com.atlantbh.cinemabh.logging;

import com.atlantbh.cinemabh.enums.AuthEventOutcome;
import com.atlantbh.cinemabh.enums.AuthEventType;
import com.atlantbh.cinemabh.util.RequestContextUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@Slf4j
@Getter
@Builder
public class AuthEvent {
  private static final ObjectMapper objectMapper =
      new ObjectMapper()
          .setSerializationInclusion(JsonInclude.Include.NON_NULL)
          .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule())
          .disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  @Builder.Default private final String ip = RequestContextUtils.getClientIp();
  @Builder.Default private final Instant timestamp = Instant.now();
  @Builder.Default private final String traceId = MDC.get("traceId");
  private AuthEventType type;
  private AuthEventOutcome outcome;
  private Long userId;
  private String email;
  private String detail;
  private String phoneNumber;

  public static AuthEventBuilder builder(
      @NonNull AuthEventType type, @NonNull AuthEventOutcome outcome) {
    return new AuthEventBuilder().type(type).outcome(outcome);
  }

  @Override
  public String toString() {
    try {
      return objectMapper.writeValueAsString(this);
    } catch (JsonProcessingException e) {
      return super.toString();
    }
  }
}
