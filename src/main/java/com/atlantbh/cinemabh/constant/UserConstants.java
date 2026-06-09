package com.atlantbh.cinemabh.constant;

public class UserConstants {
  public static final int EMAIL_MIN_LENGTH = 8;
  public static final int EMAIL_MAX_LENGTH = 254;
  public static final int FIRST_NAME_MAX_LENGTH = 254;
  public static final int LAST_NAME_MAX_LENGTH = 254;
  public static final int PHONE_NUMBER_MIN_LENGTH = 7;
  public static final int PHONE_NUMBER_MAX_LENGTH = 15;
  public static final int STREET_MAX_LENGTH = 150;

  private UserConstants() {}
}
// todo pitati ahmedina koje koristiti
/*
"password_hash" VARCHAR(255)        NOT NULL,
"first_name"    VARCHAR(100)        NOT NULL,
"last_name"     VARCHAR(80)         NOT NULL,
"phone_number"  VARCHAR(25) UNIQUE,
"email"         VARCHAR(255) UNIQUE NOT NULL,
"street"        VARCHAR(150),
"image_path"    VARCHAR(255),*/
