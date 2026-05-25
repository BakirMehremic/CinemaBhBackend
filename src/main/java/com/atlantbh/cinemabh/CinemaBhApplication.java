package com.atlantbh.cinemabh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class CinemaBhApplication {

  public static void main(String[] args) {
    SpringApplication.run(CinemaBhApplication.class, args);
  }
}
