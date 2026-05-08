package com.atlantbh.cinemabh.validator.password;

import com.atlantbh.cinemabh.exception.InvalidRequestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class PwnedPasswordValidator {
  private static final String PWNED_API = "https://api.pwnedpasswords.com/range/";
  private final RestTemplate restTemplate;
  private final Logger logger = LoggerFactory.getLogger(PwnedPasswordValidator.class);

  public void validatePasswordPwned(String password) {
    String hash = toSha1(password);

    String response = restTemplate.getForObject(PWNED_API + hash.substring(0, 5), String.class);

    if (response == null) {
      logger.warn("Did not get response form {}", PWNED_API);
      throw new RuntimeException("Could not validate password");
    }

    if (response.toUpperCase().contains(hash.substring(5).toUpperCase())) {
      throw new InvalidRequestException("Password has been compromised");
    }
  }

  private String toSha1(String input) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-1");
      byte[] encodedHash = digest.digest(input.getBytes());
      return HexFormat.of().formatHex(encodedHash).toUpperCase();
    } catch (NoSuchAlgorithmException e) {
      logger.warn("Sha 1 algorithm not found");
      throw new RuntimeException("Could not get SHA 1");
    }
  }
}
