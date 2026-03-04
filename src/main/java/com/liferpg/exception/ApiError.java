package com.liferpg.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
  * Standard API error response.
   */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

  private String message;
  private Object details;

  /**
   * Creates error with only message.
   */
  public ApiError(String message) {
    this.message = message;
  }
}
