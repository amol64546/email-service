package com.test.emailtest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // Handle the EmailLimitExceededException
  @ExceptionHandler(EmailLimitExceededException.class)
  public ResponseEntity<ErrorResponse> handleEmailLimitExceeded(EmailLimitExceededException ex) {
    // Create a custom error response
    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.FORBIDDEN.value(),
        ex.getMessage()
    );

    // Return a response with a status of FORBIDDEN (HTTP 403)
    return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler(EmailRequestNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(EmailRequestNotFoundException ex) {
    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        ex.getMessage()
    );

    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }
}
