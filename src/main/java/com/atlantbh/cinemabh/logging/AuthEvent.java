package com.atlantbh.cinemabh.logging;

import com.atlantbh.cinemabh.enums.AuthEventOutcome;
import com.atlantbh.cinemabh.enums.AuthEventType;
import com.atlantbh.cinemabh.util.RequestContextUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class AuthEvent {
  private static final ObjectMapper objectMapper =
      new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

  @Builder.Default private final String ip = RequestContextUtils.getClientIp();
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
      return toString();
    }
  }
}
