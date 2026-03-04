package com.liferpg.exception;

/**
  * Signals a 409 conflict.
   */
public class ConflictException extends RuntimeException {

  /**
    * Creates conflict exception.
     */
  public ConflictException(String message) {
    super(message);
  }
}
