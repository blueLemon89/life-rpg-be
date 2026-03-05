package com.liferpg.service.auth;

import org.springframework.security.core.Authentication;
import com.liferpg.dto.response.MeResponse;

/**
  * Current-user endpoint service contract.
   */
public interface IMeService {

  /**
    * Resolves the authenticated user from security context.
     */
  MeResponse me(Authentication authentication);
}
