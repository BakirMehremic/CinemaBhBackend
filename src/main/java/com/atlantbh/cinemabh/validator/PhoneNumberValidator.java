package com.atlantbh.cinemabh.validator;

import com.atlantbh.cinemabh.enums.AuthEventOutcome;
import com.atlantbh.cinemabh.enums.AuthEventType;
import com.atlantbh.cinemabh.exception.InvalidRequestException;
import com.atlantbh.cinemabh.logging.AuthEvent;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PhoneNumberValidator {
  private final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

  public String validateAndNormalize(String phoneNumber) {
    try {
      Phonenumber.PhoneNumber parsed;

      if (phoneNumber.startsWith("+")) {
        parsed = phoneUtil.parse(phoneNumber, null);
      } else {
        parsed = phoneUtil.parse(phoneNumber, "BA");
      }

      if (!phoneUtil.isValidNumber(parsed)) {
        log.warn(
            "{}",
            AuthEvent.builder(AuthEventType.PHONE_NUMBER_VALIDATION, AuthEventOutcome.FAILURE)
                .detail("Invalid phone number sumbitted")
                .detail("Phone number: " + phoneNumber)
                .build());
        throw new InvalidRequestException("Invalid phone number");
      }

      return phoneUtil.format(parsed, PhoneNumberUtil.PhoneNumberFormat.E164);
    } catch (NumberParseException e) {
      log.warn(
          "{}",
          AuthEvent.builder(AuthEventType.PHONE_NUMBER_VALIDATION, AuthEventOutcome.FAILURE)
              .detail("Could not parse phone number")
              .detail("Phone number: " + phoneNumber)
              .build());
      throw new InvalidRequestException("Invalid phone number");
    }
  }
}
