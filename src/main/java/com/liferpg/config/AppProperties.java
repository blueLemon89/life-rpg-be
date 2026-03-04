package com.liferpg.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
  * App-level custom properties.
   */
@Getter
@Setter
@ConfigurationProperties(prefix = "app")
public class AppProperties {

  private final Auth auth = new Auth();
  private final Cors cors = new Cors();

  /**
    * Authentication property group.
     */
  @Getter
  @Setter
  public static class Auth {
    private String jwtSecret = "dev-secret-change-me";
    private String cookieName = "session";
    private long cookieMaxAgeSeconds = 604800;
    private boolean cookieSecure = false;
    private String cookieSameSite = "Lax";
  }

  /**
    * CORS property group.
     */
  @Getter
  @Setter
  public static class Cors {
    private String allowedOrigin = "http://localhost:3000";
  }
}
