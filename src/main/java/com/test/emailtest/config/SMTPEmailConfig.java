package com.test.emailtest.config;

import com.test.emailtest.Provider.GmailProvider;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SMTPEmailConfig {

  private final EmailProperties emailConfigProperties;
  private final GmailProvider gmailProvider;

  @Bean
  public JavaMailSenderImpl javaMailSender() {
    log.info("----EmailConfig class : javaMailSender method ----");
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(emailConfigProperties.getHost());
    mailSender.setPort(emailConfigProperties.getPort());
    mailSender.setUsername(emailConfigProperties.getEmailId());
    mailSender.setPassword(emailConfigProperties.getPassword());
    mailSender.setJavaMailProperties(getMailProperties(emailConfigProperties.getProvider()));
    log.info("----javamailSender config bean created ----");
    return mailSender;
  }

  private Properties getMailProperties(String provider) {
    Properties properties = new Properties();
    if ("GMAIL".equalsIgnoreCase(provider)) {
      getGmailProperties(properties);
    }
    return properties;
  }

  private void getGmailProperties(Properties properties) {
    for (String key : gmailProvider.getGmailProperties().keySet()) {
      properties.setProperty(key, gmailProvider.getGmailProperties().get(key));
    }
  }
}
