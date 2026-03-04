package com.liferpg.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response for successful login.
  */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

  private UUID userId;
}
