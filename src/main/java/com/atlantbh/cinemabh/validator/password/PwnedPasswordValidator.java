package com.atlantbh.cinemabh.validator.password;

import static com.atlantbh.cinemabh.util.HashingUtils.toSha1;

import com.atlantbh.cinemabh.exception.InvalidRequestException;
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
      throw new RuntimeException("Could not validate password");
    }

    if (response.toUpperCase().contains(hash.substring(5).toUpperCase())) {
      throw new InvalidRequestException("Password has been compromised");
    }
  }
}
