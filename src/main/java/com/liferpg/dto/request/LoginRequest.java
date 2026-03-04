package com.liferpg.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Payload for user login.
  */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

  @NotBlank
  @Email
  private String email;

  @NotBlank
  @Size(min = 6)
  private String password;
}
