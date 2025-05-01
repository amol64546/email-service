package com.test.emailtest.exception;

public class EmailLimitExceededException extends RuntimeException {

  public EmailLimitExceededException(String message) {
    super(message);
  }
}
