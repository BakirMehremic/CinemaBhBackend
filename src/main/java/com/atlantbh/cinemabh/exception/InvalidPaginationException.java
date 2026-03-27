package com.atlantbh.cinemabh.exception;

public class InvalidPaginationException extends RuntimeException {

  public InvalidPaginationException() {
    super("Invalid pagination parameters");
  }

  public InvalidPaginationException(String message) {
    super(message);
  }
}
