package com.atlantbh.cinemabh.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class RequestContextUtils {
  public static String getClientIp() {
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

    if (attributes != null) {
      HttpServletRequest request = attributes.getRequest();

      String xForwardedFor = request.getHeader("X-Forwarded-For");

      if (xForwardedFor != null && !xForwardedFor.isBlank()) {
        return xForwardedFor.split(",")[0].trim();
      }

      return request.getRemoteAddr();
    }
    return "unknown";
  }
}
