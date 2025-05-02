package com.test.emailtest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class EmailTestApplication {

  public static void main(String[] args) {
    SpringApplication.run(EmailTestApplication.class, args);
  }

}
