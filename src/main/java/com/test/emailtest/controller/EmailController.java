package com.test.emailtest.controller;

import com.test.emailtest.entity.EmailRequest;
import com.test.emailtest.exception.EmailLimitExceededException;
import com.test.emailtest.exception.EmailRequestNotFoundException;
import com.test.emailtest.repo.EmailRepository;
import com.test.emailtest.service.EmailService;
import com.test.emailtest.service.EmailTrackingService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmailController {

  private final EmailService emailService;
  private final EmailTrackingService emailTrackingService;
  private final EmailRepository emailRepository;


  @GetMapping("/email-count-today")
  public ResponseEntity<ModelMap> getEmailCountToday() {
    ModelMap response = new ModelMap()
        .addAttribute("today's used limit", emailTrackingService.getEmailCountForToday())
        .addAttribute("today's remaining limit", 100 - emailTrackingService.getEmailCountForToday())
        .addAttribute("today's total limit", 100);
    return ResponseEntity.status(HttpStatus.OK)
        .body(response);
  }

  @GetMapping("/email-status/{id}")
  public ResponseEntity<EmailRequest> getEmailStatus(
      @PathVariable String id
  ) {
    Optional<EmailRequest> emailRequest = emailRepository.findById(id);
    if (emailRequest.isEmpty()) {
      throw new EmailRequestNotFoundException("Email request not found with given id.");
    }
    return ResponseEntity.status(HttpStatus.OK)
        .body(emailRequest.get());
  }

  @PostMapping(path = "/send-email")
  public ResponseEntity<ModelMap> sendEmail(
      @ModelAttribute EmailRequest emailRequest, HttpServletRequest httpServletRequest) {

    emailRequest.setId(UUID.randomUUID().toString());
    ModelMap response = new ModelMap()
        .addAttribute("id", emailRequest.getId());
    try {

      if (emailTrackingService.getEmailCountForToday() >= 100) {
        throw new EmailLimitExceededException("The daily email limit of 100 has been reached.");
      }

      emailRequest.setFromEmail(httpServletRequest.getHeader("from"));
      emailRequest.setApiKey(httpServletRequest.getHeader("api-key"));
      emailService.sendEmail(emailRequest);
      response.addAttribute("message", "Email queued successfully.");
      return ResponseEntity.status(HttpStatus.ACCEPTED)
          .body(response);
    } catch (Exception e) {
      response.addAttribute("message", "Failed to send email.")
          .addAttribute("error", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(response);
    }
  }
}
