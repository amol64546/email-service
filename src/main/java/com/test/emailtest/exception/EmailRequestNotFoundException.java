package com.test.emailtest.exception;

public class EmailRequestNotFoundException extends RuntimeException {


  public EmailRequestNotFoundException(String message) {
    super(message);
  }
}