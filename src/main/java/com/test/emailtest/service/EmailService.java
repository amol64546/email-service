package com.test.emailtest.service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import com.test.emailtest.entity.EmailRequest;
import com.test.emailtest.repo.EmailRepository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

  private final EmailRepository emailRepository;
  private final EmailTrackingService emailTrackingService;

  @Async
  public void sendEmail(EmailRequest emailRequest) throws IOException {

    System.out.println("-----------------------------------");

    int total = emailRequest.getTo().size();
    int success = 0;

    for (String recipient : emailRequest.getTo()) {
      // Create the email for each recipient individually
      Mail mail = new Mail();
      mail.setFrom(new Email(emailRequest.getFromEmail()));
      mail.setSubject(emailRequest.getSubject());
      mail.addContent(new Content("text/plain", emailRequest.getBody()));

      // Personalization for each recipient
      Personalization personalization = new Personalization();
      personalization.addTo(new Email(recipient));
      mail.addPersonalization(personalization);

      // Send email
      Request request = new Request();
      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());
      SendGrid sendGrid = new SendGrid(emailRequest.getApiKey());
      Response response = sendGrid.api(request);

      if (response.getStatusCode() >= 400) {
        log.error("Failed to send email to -> {}: {}", recipient, response.getBody());

      } else {
        success++;
        emailTrackingService.incrementEmailCount();
        System.out.println("Email sent to -> " + recipient);
      }
    }

    emailRequest.setTotal(total);
    emailRequest.setSuccess(success);
    emailRequest.setFailed(total - success);
    emailRepository.save(emailRequest);

    System.out.println("-----------------------------------");
    System.out.println("Total Email: " + total);
    System.out.println("Success email: " + success);
    System.out.println("Failed email: " + (total - success));
  }

}
