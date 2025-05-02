package com.test.emailtest.service;

import com.test.emailtest.entity.EmailRequest;
import com.test.emailtest.repo.EmailRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

  private final EmailRepository emailRepository;
  private final EmailTrackingService emailTrackingService;
  private final JavaMailSenderImpl mailSender;

  @Async
  public void saveEmailRequest(EmailRequest emailRequest) {
    if (!emailRequest.getTo().isEmpty()) {
      // send them all in one SMTP session
      emailTrackingService.incrementEmailCount(emailRequest.getTo().size());
    }
    emailRepository.save(emailRequest);
  }

  @Async
  public void sendEmail(EmailRequest emailRequest, String toEmail) {
    try {
      MimeMessage msg = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(msg, true);

      helper.setSubject(emailRequest.getSubject());
      helper.setTo(toEmail);
      helper.setText(emailRequest.getBody(), false);

      // Add attachment (MultipartFile)
      if (emailRequest.getAttachment() != null && !emailRequest.getAttachment().isEmpty()) {

        String fileName = emailRequest.getAttachment().getOriginalFilename();
        try {
          // Convert MultipartFile to InputStreamSource
          byte[] fileBytes = emailRequest.getAttachment()
              .getBytes(); // assume this is part of the request
          InputStreamSource inputStreamSource = new ByteArrayResource(fileBytes);

          // Add the attachment using the new addAttachment method
          helper.addAttachment(Objects.requireNonNull(fileName), inputStreamSource);
        } catch (IOException e) {
          log.error("Error reading attachment input stream: {}", e.getMessage());
        }
      }
      mailSender.send(msg);
      log.info("Email sent to -> {}", toEmail);
    } catch (MessagingException e) {
      log.error("Failed to prepare invitation for {}: {}", toEmail, e.getMessage());
    }
  }
}
