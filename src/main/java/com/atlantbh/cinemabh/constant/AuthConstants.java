package com.atlantbh.cinemabh.constant;

public class AuthConstants {
  public static final int PASSWORD_MIN_LENGTH = 8;
  public static final int PASSWORD_MAX_LENGTH = 48;
  public static final int VERIFICATION_CODE_LENGTH = 6;
  public static final int PASSWORD_HASH_LENGTH = 60;
  public static final int REFRESH_TOKEN_HASH_LENGTH = 44;
  public static final int VERIFICATION_CODE_HASH_LENGTH = 44;

  public static final String ACCESS_TOKEN_NAME = "access_token";
  public static final String REFRESH_TOKEN_NAME = "refresh_token";

  public static final String PWNED_API = "https://api.pwnedpasswords.com/range/";

  public static final int MX_RESOLVER_TIMEOUT_SECONDS = 2;

  private AuthConstants() {}
}
