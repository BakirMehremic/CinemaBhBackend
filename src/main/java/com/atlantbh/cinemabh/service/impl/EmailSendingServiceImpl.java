package com.atlantbh.cinemabh.service.impl;

import com.atlantbh.cinemabh.service.EmailSendingService;
import com.resend.Resend;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSendingServiceImpl implements EmailSendingService {
  private final Resend resend;

  @Value("${resend.from-email}")
  private String fromEmail;

  @Override
  public void sendVerificationEmail(String recipient, String subject, String code) {
    log.warn("code is {}", code);
    return; // uncomment later
/*    String htmlBody =
        "<h1>Verification Code</h1>" + "<h2>Your code is: <strong>" + code + "</strong></h2>";

    CreateEmailOptions params =
        CreateEmailOptions.builder()
            .from(fromEmail)
            .to(recipient)
            .subject(subject)
            .html(htmlBody)
            .text("Your verification code is: " + code)
            .build();

    try {
      CreateEmailResponse response = resend.emails().send(params);
    } catch (ResendException e) {
      log.error("Failed to send email, {}", e.getMessage());
      throw new ServiceUnavailableException("Email could not be sent.");
    }*/
  }
}
