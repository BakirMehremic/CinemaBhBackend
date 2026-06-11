package com.atlantbh.cinemabh.filter;

import com.atlantbh.cinemabh.dto.internal.TokenClaims;
import com.atlantbh.cinemabh.service.CookieService;
import com.atlantbh.cinemabh.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
  private final CookieService cookieService;
  private final JwtService jwtService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      filterChain.doFilter(request, response);
      return;
    }

    Optional<String> token = cookieService.extractAccessToken(request);

    if (token.isEmpty()) {
      filterChain.doFilter(request, response);
      return;
    }

    String accessToken = token.get();

    if (jwtService.isTokenValid(accessToken)) {
      TokenClaims claims = jwtService.parseToken(accessToken);

      List<SimpleGrantedAuthority> authorities =
          List.of(new SimpleGrantedAuthority("ROLE_" + claims.role()));

      UsernamePasswordAuthenticationToken auth =
          new UsernamePasswordAuthenticationToken(claims.userId(), null, authorities);
      SecurityContextHolder.getContext().setAuthentication(auth);
    } else {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json");
      response.getWriter().write("Invalid or expired access token");
      return;
    }

    filterChain.doFilter(request, response);
  }
}
