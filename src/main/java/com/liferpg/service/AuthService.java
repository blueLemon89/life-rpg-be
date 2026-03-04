package com.liferpg.service;

/**
  * Authentication service contract.
   */
public interface AuthService {

  /**
    * Registers a new user and profile.
     */
  AuthResult register(String email, String password, String name);

  /**
    * Authenticates an existing user.
     */
  AuthResult login(String email, String password);
}
