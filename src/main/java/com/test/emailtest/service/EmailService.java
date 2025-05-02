package com.test.emailtest.service;

import com.test.emailtest.entity.EmailRequest;
import com.test.emailtest.repo.EmailRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

  private final EmailRepository emailRepository;
  private final EmailTrackingService emailTrackingService;
  private final JavaMailSenderImpl mailSender;

  @Async
  public void sendEmail(EmailRequest emailRequest) {
    List<MimeMessage> messages = new ArrayList<>(emailRequest.getTo().size());

    for (String email : emailRequest.getTo()) {
      try {
        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        helper.setSubject(emailRequest.getSubject());
        helper.setTo(email);
        helper.setText(emailRequest.getBody(), false);

        // Add attachment (MultipartFile)
        MultipartFile attachment = emailRequest.getAttachment(); // assume this is part of the request
        if (attachment != null && !attachment.isEmpty()) {
          // Attach the file to the email
          helper.addAttachment(Objects.requireNonNull(attachment.getOriginalFilename()),
              attachment); // Uses MultipartFile directly
        }

        messages.add(msg);
      } catch (MessagingException e) {
        log.error("Failed to prepare invitation for {}: {}", email, e.getMessage());
      }
    }

    if (!messages.isEmpty()) {
      // send them all in one SMTP session
      mailSender.send(messages.toArray(new MimeMessage[0]));
      emailTrackingService.incrementEmailCount(messages.size());
      log.info("Total emails sent {}", messages.size());
    }
    emailRepository.save(emailRequest);
  }

}
