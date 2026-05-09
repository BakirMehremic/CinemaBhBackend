package com.atlantbh.cinemabh.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HexFormat;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HashingUtils {

  public static String toSha1(String input) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-1");
      byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
      return HexFormat.of().formatHex(encodedHash).toUpperCase();
    } catch (NoSuchAlgorithmException e) {
      log.warn("could not get instance of SHA-1, {}", e.getMessage());
      throw new RuntimeException("Could not get hash");
    }
  }

  public static String toSha256(String token) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(hash);
    } catch (NoSuchAlgorithmException e) {
      log.warn("could not get instance of SHA-256, {}", e.getMessage());
      throw new RuntimeException("Error hashing token", e);
    }
  }
}
