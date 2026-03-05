package com.liferpg.service.auth;

import com.liferpg.dto.request.RegisterRequest;

/**
  * Authentication service contract.
   */
public interface IAuthService {

  /**
    * Registers a new user and profile.
     */
  AuthResult register(RegisterRequest request);

  /**
    * Authenticates an existing user.
     */
  AuthResult login(String email, String password);
}
