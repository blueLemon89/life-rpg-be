package com.liferpg.service.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import com.liferpg.entity.User;
import com.liferpg.entity.UserProfile;

/**
  * Result holder for auth operations.
   */
@Getter
@AllArgsConstructor
public class AuthResult {

  private final User user;
  private final UserProfile profile;
}
