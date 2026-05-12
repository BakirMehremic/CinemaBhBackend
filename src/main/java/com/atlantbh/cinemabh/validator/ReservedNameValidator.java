package com.atlantbh.cinemabh.validator;

import com.atlantbh.cinemabh.enums.AuthEventOutcome;
import com.atlantbh.cinemabh.enums.AuthEventType;
import com.atlantbh.cinemabh.exception.InvalidRequestException;
import com.atlantbh.cinemabh.logging.AuthEvent;
import java.util.Arrays;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReservedNameValidator {
  private static final Set<String> RESERVED_NAMES =
      Set.of(
          "root",
          "guest",
          "admin",
          "administrator",
          "info",
          "support",
          "no-reply",
          "noreply",
          "system",
          "null",
          "undefined",
          "anonymous");

  public static void validate(String... inputs) {
    Arrays.stream(inputs)
        .map(input -> input.trim().toLowerCase())
        .filter(RESERVED_NAMES::contains)
        .findFirst()
        .ifPresent(
            invalid -> {
              log.warn(
                  "{}",
                  AuthEvent.builder(AuthEventType.REGISTER, AuthEventOutcome.FAILURE)
                      .detail("User tried to register with reserved name")
                      .build());
              throw new InvalidRequestException(invalid + " is a reserved name.");
            });
    ;
  }
}
