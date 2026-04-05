package com.atlantbh.cinemabh.exception;

import com.atlantbh.cinemabh.dto.response.ErrorResponse;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionResponseHandler {
  private final Logger logger = LoggerFactory.getLogger(ExceptionResponseHandler.class);

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex) {
    logger.error("Exception occurred:", ex);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorResponse("Internal server error"));
  }

  @ExceptionHandler(InvalidPaginationException.class)
  public ResponseEntity<ErrorResponse> handlePaginationException(InvalidPaginationException ex) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex.getMessage()));
  }

  // handles DTO validation exceptions
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<List<ErrorResponse>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {

    List<ErrorResponse> errorResponses =
        ex.getBindingResult().getFieldErrors().stream()
            .map(error -> new ErrorResponse(error.getField() + ": " + error.getDefaultMessage()))
            .toList();

    return ResponseEntity.badRequest().body(errorResponses);
  }
}
