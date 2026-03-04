package com.liferpg.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response payload for current authenticated user.
  */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MeResponse {

  private UserPayload user;

  /**
   * User info inside me response.
    */
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UserPayload {
    private UUID id;
    private String email;
    private String name;
  }
}
