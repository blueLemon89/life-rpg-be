package com.liferpg.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response for successful registration.
  */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {

  private UUID userId;
}
