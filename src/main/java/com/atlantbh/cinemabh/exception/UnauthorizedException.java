package com.atlantbh.cinemabh.exception;

public class UnauthorizedException extends RuntimeException {
  public UnauthorizedException(String message) {
    super(message);
  }

  public UnauthorizedException() {
    super("User is not logged in;");
  }
}
