package com.liferpg.service.auth;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import com.liferpg.config.AuthenticatedUserPrincipal;
import com.liferpg.dto.response.MeResponse;
import com.liferpg.exception.UnauthorizedException;

/**
  * Current-user endpoint business logic.
   */
@Service
public class MeServiceImpl implements IMeService {

  /**
   * Resolves current user from authentication principal.
   */
  @Override
  public MeResponse me(Authentication authentication) {
    if (authentication == null
        || !(authentication.getPrincipal() instanceof AuthenticatedUserPrincipal principal)) {
      throw new UnauthorizedException("Unauthorized");
    }

    return new MeResponse(new MeResponse.UserPayload(
        principal.getUserId(),
        principal.getEmail(),
        principal.getName()));
  }
}
