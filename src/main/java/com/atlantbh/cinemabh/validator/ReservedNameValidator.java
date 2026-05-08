package com.atlantbh.cinemabh.validator;

import com.atlantbh.cinemabh.exception.InvalidRequestException;
import java.util.Arrays;
import java.util.Set;

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
              throw new InvalidRequestException(invalid + " is a reserved name.");
            });
    ;
  }
}
