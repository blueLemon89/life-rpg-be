package com.liferpg.service.auth;

import java.time.Duration;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import com.liferpg.config.AppProperties;

/**
  * Builds and clears auth session cookies.
   */
@Service
public class AuthCookieService {

  private final AppProperties.Auth authProperties;

  /**
    * Creates cookie service with app auth settings.
     */
  public AuthCookieService(AppProperties appProperties) {
    this.authProperties = appProperties.getAuth();
  }

  /**
    * Builds session cookie value header.
     */
  public String buildSessionCookie(String token) {
    return ResponseCookie.from(authProperties.getCookieName(), token)
        .httpOnly(true)
        .path("/")
        .secure(authProperties.isCookieSecure())
        .sameSite(authProperties.getCookieSameSite())
        .maxAge(Duration.ofSeconds(authProperties.getCookieMaxAgeSeconds()))
        .build()
        .toString();
  }

  /**
    * Builds cookie header that clears session cookie.
     */
  public String clearSessionCookie() {
    return ResponseCookie.from(authProperties.getCookieName(), "")
        .httpOnly(true)
        .path("/")
        .secure(authProperties.isCookieSecure())
        .sameSite(authProperties.getCookieSameSite())
        .maxAge(Duration.ZERO)
        .build()
        .toString();
  }
}
