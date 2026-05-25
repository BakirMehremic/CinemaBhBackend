package com.atlantbh.cinemabh.validator.email;

import com.atlantbh.cinemabh.exception.InvalidRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailValidator {
  private final EmailMxValidator emailMxValidator;
  private final EmailTldValidator emailTldValidator;
  private final DisposableEmailValidator disposableEmailValidator;

  private static String extractTLD(String domain) {
    String[] parts = domain.split("\\.");
    return parts[parts.length - 1];
  }

  public void validateEmail(String email) {
    if (email == null || !email.contains("@")) {
      throw new InvalidRequestException("Invalid email");
    }

    String domain = email.substring(email.indexOf("@") + 1);
    String tld = extractTLD(domain);

    if (disposableEmailValidator.isDisposable(domain)) {
      throw new InvalidRequestException("Disposable email addresses are not allowed");
    }

    if (!emailMxValidator.isValid(domain)) {
      throw new InvalidRequestException("Email domain has no valid MX records");
    }

    if (!emailTldValidator.isValidTld(tld)) {
      throw new InvalidRequestException("Invalid top-level domain");
    }
  }
}
