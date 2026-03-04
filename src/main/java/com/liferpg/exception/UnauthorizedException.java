package com.liferpg.exception;

/**
  * Signals a 401 unauthorized error.
   */
public class UnauthorizedException extends RuntimeException {

  /**
    * Creates unauthorized exception.
     */
  public UnauthorizedException(String message) {
    super(message);
  }
}
