package com.atlantbh.cinemabh.service.impl;

import com.atlantbh.cinemabh.config.properties.ResendProperties;
import com.atlantbh.cinemabh.service.EmailSendingService;
import com.resend.Resend;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSendingServiceImpl implements EmailSendingService {
  private final Resend resend;
  private final ResendProperties resendProperties;

  @Override
  public void sendVerificationEmail(String recipient, String subject, String code) {
    log.warn("code is {}", code);
    return;
    /*    String htmlBody =
        "<h1>Verification Code</h1>" + "<h2>Your code is: <strong>" + code + "</strong></h2>";

    CreateEmailOptions params =
        CreateEmailOptions.builder()
            .from(resendProperties.getFromEmail())
            .to(recipient)
            .subject(subject)
            .html(htmlBody)
            .text("Your verification code is: " + code)
            .build();

    try {
      CreateEmailResponse response = resend.emails().send(params);
      log.warn(
          "{}",
          AuthEvent.builder(AuthEventType.VERIFY_ACCOUNT, AuthEventOutcome.SUCCESS)
              .email(recipient)
              .build());
    } catch (ResendException e) {
      log.error("could not send email {}", e.getMessage(), e);
      AuthEvent.builder(AuthEventType.VERIFY_ACCOUNT, AuthEventOutcome.FAILURE)
          .email(recipient)
          .detail("Could not send email")
          .build();
      throw new ServiceUnavailableException("Email could not be sent.");
    }*/
  }
}
