
/*
 * Copyright (c) 2024. Author :: developer Gaian Solutions Pvt Ltd. All rights reserved.
 */
package com.test.emailtest.Provider;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class GmailProvider implements IEmailProvider {

  public Map<String, String> getGmailProperties() {
    Map<String, String> propertiesMap = new HashMap<>();
    propertiesMap.put("mail.smtp.auth", "true");
    propertiesMap.put("mail.smtp.starttls.enable", "true");
    propertiesMap.put("mail.smtp.starttls.required", "true");
    propertiesMap.put("mail.smtp.ssl.protocols", "TLSv1.2");
    return propertiesMap;
  }
}
