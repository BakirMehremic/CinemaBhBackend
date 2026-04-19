package com.atlantbh.cinemabh.exception;

public class InvalidRequestException extends RuntimeException {
  public InvalidRequestException(String message) {
    super(message);
  }

  public InvalidRequestException() {
    super("Invalid request parameters passed");
  }
}
