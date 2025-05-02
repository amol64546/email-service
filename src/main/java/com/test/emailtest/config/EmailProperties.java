
/*
 * Copyright (c) 2024. Author :: developer Gaian Solutions Pvt Ltd. All rights reserved.
 */
package com.test.emailtest.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "email.property", ignoreInvalidFields = true
)
public class EmailProperties {

  private String host;
  private int port;
  private String emailId;
  private String password;
  private String provider;
  private String subject;
}
