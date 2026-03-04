package com.liferpg.service;

import org.springframework.security.core.Authentication;
import com.liferpg.dto.response.MeResponse;

/**
  * Current-user endpoint service contract.
   */
public interface MeService {

  /**
    * Resolves the authenticated user from security context.
     */
  MeResponse me(Authentication authentication);
}
