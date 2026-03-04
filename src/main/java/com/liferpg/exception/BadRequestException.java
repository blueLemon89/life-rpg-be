package com.liferpg.exception;

/**
  * Signals a 400 bad request.
   */
public class BadRequestException extends RuntimeException {

  /**
    * Creates bad request exception.
     */
  public BadRequestException(String message) {
    super(message);
  }
}
