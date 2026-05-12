package com.atlantbh.cinemabh.validator.password;

import static com.atlantbh.cinemabh.util.HashingUtils.toSha1;

import com.atlantbh.cinemabh.enums.AuthEventOutcome;
import com.atlantbh.cinemabh.enums.AuthEventType;
import com.atlantbh.cinemabh.exception.InvalidRequestException;
import com.atlantbh.cinemabh.exception.ServiceUnavailableException;
import com.atlantbh.cinemabh.logging.AuthEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class PwnedPasswordValidator {
  private static final String PWNED_API = "https://api.pwnedpasswords.com/range/";
  private final RestTemplate restTemplate;

  public void validatePasswordPwned(String password) {
    String hash = toSha1(password);

    String response = restTemplate.getForObject(PWNED_API + hash.substring(0, 5), String.class);

    if (response == null) {
      log.warn("Did not get response form {}", PWNED_API);
      throw new ServiceUnavailableException("Could not validate password");
    }

    if (response.toUpperCase().contains(hash.substring(5).toUpperCase())) {
      log.warn(
          "{}",
          AuthEvent.builder(AuthEventType.PASSWORD_VALIDATION, AuthEventOutcome.FAILURE)
              .detail("User tried to use pwned password")
              .build());
      throw new InvalidRequestException("Password has been compromised");
    }
  }
}
