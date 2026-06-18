package com.atlantbh.cinemabh.config;

import jakarta.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Component
@ConfigurationProperties(prefix = "app.security.reserved")
public class ReservedNamesConfig {

  private static final Set<String> DEFAULTS =
      Set.of(
          "admin",
          "administrator",
          "root",
          "system",
          "sys",
          "superuser",
          "operator",
          "owner",
          "hostmaster",
          "webmaster",
          "auth",
          "login",
          "logout",
          "signin",
          "signup",
          "register",
          "account",
          "accounts",
          "security",
          "api",
          "service",
          "support",
          "help",
          "billing",
          "payments",
          "sales",
          "contact",
          "team",
          "staff",
          "customer",
          "success",
          "info",
          "mail",
          "mailer",
          "postmaster",
          "no-reply",
          "noreply",
          "donotreply",
          "guest",
          "anonymous",
          "unknown",
          "user",
          "test",
          "demo",
          "sample",
          "default",
          "null",
          "undefined",
          "none",
          "true",
          "false",
          "admin1",
          "admin123",
          "adminx",
          "root-user",
          "support-team");

  private Set<String> names = new HashSet<>();

  @PostConstruct
  void init() {
    names.addAll(DEFAULTS);
  }
}
