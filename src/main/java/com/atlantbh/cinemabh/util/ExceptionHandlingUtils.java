package com.atlantbh.cinemabh.util;

import jakarta.validation.Path;

public final class ExceptionHandlingUtils {
  private ExceptionHandlingUtils() {}

  public static String extractFieldName(Path propertyPath) {
    String path = propertyPath.toString();
    return path.substring(path.lastIndexOf('.') + 1);
  }
}
