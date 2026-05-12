package com.atlantbh.cinemabh.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class RequestContextUtils {
  public static String getClientIp() {
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (attributes != null) {
      HttpServletRequest request = attributes.getRequest();

      String remoteAddr = request.getHeader("X-Forwarded-For");
      if (remoteAddr == null || remoteAddr.isEmpty()) {
        remoteAddr = request.getRemoteAddr();
      }
      return remoteAddr;
    }
    return "unknown";
  }
}
