package com.liferpg.config;

import java.io.IOException;
import java.util.Collections;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
  * Reads JWT from auth cookie and sets authentication in security context.
   */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final String cookieName;

  /**
    * Creates JWT auth filter.
     */
  public JwtAuthFilter(JwtService jwtService, AppProperties appProperties) {
    this.jwtService = jwtService;
    this.cookieName = appProperties.getAuth().getCookieName();
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain
  ) throws ServletException, IOException {
    String token = resolveToken(request.getCookies());
    if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      try {
        var claims = jwtService.verify(token);
        var principal = jwtService.toPrincipal(claims);
        var authentication = new UsernamePasswordAuthenticationToken(
            principal,
            null,
            Collections.emptyList()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
      } catch (RuntimeException ignored) {
        SecurityContextHolder.clearContext();
      }
    }

    filterChain.doFilter(request, response);
  }

  private String resolveToken(Cookie[] cookies) {
    if (cookies == null) {
      return null;
    }
    for (Cookie cookie : cookies) {
      if (cookieName.equals(cookie.getName())) {
        return cookie.getValue();
      }
    }
    return null;
  }
}
