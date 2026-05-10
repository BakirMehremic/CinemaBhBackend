package com.atlantbh.cinemabh.filter;

import com.atlantbh.cinemabh.service.CookieService;
import com.atlantbh.cinemabh.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
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

    String token = cookieService.extractAccessToken(request);

    if (token != null && jwtService.isTokenValid(token)) {
      String role = jwtService.extractRole(token);

      List<SimpleGrantedAuthority> authorities =
          List.of(new SimpleGrantedAuthority("ROLE_" + role));

      UsernamePasswordAuthenticationToken auth =
          new UsernamePasswordAuthenticationToken(
              jwtService.extractUserId(token), null, authorities);
      SecurityContextHolder.getContext().setAuthentication(auth);
    }

    filterChain.doFilter(request, response);
  }
}
