package com.liferpg.config;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

/**
  * Signs and verifies JWT tokens for session auth.
   */
@Service
public class JwtService {

  private final SecretKey key;
  private final long expirySeconds;

  /**
    * Creates JWT service using configured secret and expiration.
     */
  public JwtService(AppProperties appProperties) {
    this.key = createKey(appProperties.getAuth().getJwtSecret());
    this.expirySeconds = appProperties.getAuth().getCookieMaxAgeSeconds();
  }

  /**
    * Signs a JWT token for user session.
     */
  public String sign(UUID userId, String email, String name) {
    Instant now = Instant.now();
    Instant expiresAt = now.plusSeconds(expirySeconds);
    return Jwts.builder()
        .subject(userId.toString())
        .claim("email", email)
        .claim("name", name)
        .issuedAt(Date.from(now))
        .expiration(Date.from(expiresAt))
        .signWith(key)
        .compact();
  }

  /**
    * Verifies a JWT token and returns claims.
     */
  public Claims verify(String token) {
    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  /**
    * Converts claims to authenticated principal.
     */
  public AuthenticatedUserPrincipal toPrincipal(Claims claims) {
    return new AuthenticatedUserPrincipal(
        UUID.fromString(claims.getSubject()),
        claims.get("email", String.class),
        claims.get("name", String.class)
    );
  }

  private SecretKey createKey(String secret) {
    try {
      byte[] digest = MessageDigest.getInstance("SHA-256")
          .digest(secret.getBytes(StandardCharsets.UTF_8));
      return Keys.hmacShaKeyFor(digest);
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("Cannot initialize JWT key", e);
    }
  }
}
