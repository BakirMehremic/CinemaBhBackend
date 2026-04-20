package com.atlantbh.cinemabh.validator;

import com.atlantbh.cinemabh.exception.InvalidRequestException;
import java.util.Arrays;

public class IdValidator {
  private IdValidator() {}

  public static void validateIdPositive(int... ids) {
    if (Arrays.stream(ids).anyMatch(id -> id < 1)) {
      throw new InvalidRequestException("Id must be positive");
    }
  }

  public static void validateIdPositive(long... ids) {
    if (Arrays.stream(ids).anyMatch(id -> id < 1)) {
      throw new InvalidRequestException("Id must be positive");
    }
  }

  public static void validateIdNullOrPositive(Long... ids) {
    if (Arrays.stream(ids).anyMatch(id -> id != null && id < 1)) {
      throw new InvalidRequestException("Id must be null or positive");
    }
  }
}
