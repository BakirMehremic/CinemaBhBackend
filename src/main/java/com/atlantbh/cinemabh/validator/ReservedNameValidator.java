package com.atlantbh.cinemabh.validator;

import com.atlantbh.cinemabh.config.ReservedNamesConfig;
import com.atlantbh.cinemabh.enums.AuthEventOutcome;
import com.atlantbh.cinemabh.enums.AuthEventType;
import com.atlantbh.cinemabh.exception.InvalidRequestException;
import com.atlantbh.cinemabh.logging.AuthEvent;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReservedNameValidator {
  private final ReservedNamesConfig reservedNamesConfig;

  public void validate(String... inputs) {
    Arrays.stream(inputs)
        .map(input -> input.trim().toLowerCase())
        .filter(reservedNamesConfig.getNames()::contains)
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
  }
}
