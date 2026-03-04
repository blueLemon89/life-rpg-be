package com.liferpg.config;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Authenticated principal extracted from JWT claims.
  */
@Getter
@AllArgsConstructor
public class AuthenticatedUserPrincipal {

  private final UUID userId;
  private final String email;
  private final String name;
}
