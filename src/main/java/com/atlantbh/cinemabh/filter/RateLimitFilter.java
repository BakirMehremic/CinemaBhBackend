package com.atlantbh.cinemabh.filter;

import static com.atlantbh.cinemabh.constant.RateLimitConstants.REQUESTS_PER_MINUTE;

import com.atlantbh.cinemabh.util.RequestContextUtils;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RateLimitFilter implements Filter {
  private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    String ip = RequestContextUtils.getClientIp();

    Bucket bucket =
        cache.computeIfAbsent(
            ip,
            key ->
                Bucket.builder()
                    .addLimit(
                        Bandwidth.classic(
                            REQUESTS_PER_MINUTE,
                            Refill.intervally(REQUESTS_PER_MINUTE, Duration.ofMinutes(1))))
                    .build());

    if (bucket.tryConsume(1)) {
      chain.doFilter(request, response);
    } else {
      log.warn("User with ip: {} submitted too many requests", ip);
      response.setStatus(429);
      response.getWriter().write("Rate limit exceeded. Please try again later.");
    }
  }
}
