package com.atlantbh.cinemabh.validator.password;

import com.atlantbh.cinemabh.enums.AuthEventOutcome;
import com.atlantbh.cinemabh.enums.AuthEventType;
import com.atlantbh.cinemabh.exception.InvalidRequestException;
import com.atlantbh.cinemabh.logging.AuthEvent;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.passay.*;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PasswordComplexityValidator {
  private final PasswordValidator validator =
      new PasswordValidator(
          List.of(
              new LengthRule(8, 48),
              new CharacterRule(EnglishCharacterData.UpperCase, 1),
              new CharacterRule(EnglishCharacterData.LowerCase, 1),
              new CharacterRule(EnglishCharacterData.Digit, 1),
              new CharacterRule(EnglishCharacterData.Special, 1),
              new WhitespaceRule(),
              new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 5, false),
              new IllegalSequenceRule(EnglishSequenceData.Numerical, 5, false)));

  public void validate(String password) {
    RuleResult result = validator.validate(new PasswordData(password));
    if (!result.isValid()) {
      log.info(
          "{}",
          AuthEvent.builder(AuthEventType.PASSWORD_VALIDATION, AuthEventOutcome.FAILURE)
              .detail("Password too simple")
              .build());
      throw new InvalidRequestException(String.join(", ", validator.getMessages(result)));
    }
  }
}
