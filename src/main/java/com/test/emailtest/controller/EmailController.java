package com.test.emailtest.controller;

import com.test.emailtest.entity.EmailRequest;
import com.test.emailtest.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmailController {

  private final EmailService emailService;


  @PostMapping(path = "/send-email")
  public ResponseEntity<ModelMap> sendEmail(
      @ModelAttribute EmailRequest emailRequest) {

    ModelMap response = new ModelMap();
    try {
      for (String email : emailRequest.getTo()) {
        emailService.sendEmail(emailRequest, email);
      }
      emailService.saveEmailRequest(emailRequest);
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
