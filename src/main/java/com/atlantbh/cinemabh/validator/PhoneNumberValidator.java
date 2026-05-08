package com.atlantbh.cinemabh.validator;

import com.atlantbh.cinemabh.exception.InvalidRequestException;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.springframework.stereotype.Component;

@Component
public class PhoneNumberValidator {
  private final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

  public void validate(String phoneNumber) {
    try {
      Phonenumber.PhoneNumber parsed;

      if (phoneNumber.startsWith("+")) {
        parsed = phoneUtil.parse(phoneNumber, null);
      } else {
        parsed = phoneUtil.parse(phoneNumber, "BA");
      }

      if (!phoneUtil.isValidNumber(parsed)) {
        throw new InvalidRequestException("Invalid phone number");
      }

    } catch (NumberParseException e) {
      throw new InvalidRequestException("Invalid phone number");
    }
  }
}
