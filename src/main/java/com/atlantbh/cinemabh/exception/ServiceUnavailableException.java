package com.atlantbh.cinemabh.exception;

public class ServiceUnavailableException extends RuntimeException {
  public ServiceUnavailableException(String message) {
    super(message);
  }

  public ServiceUnavailableException() {
    super("Request can not be handled currently, please try again later.");
  }
}
