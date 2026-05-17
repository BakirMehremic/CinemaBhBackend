package com.atlantbh.cinemabh.config;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.security")
public class ReservedNamesConfig {

  // appends names from app.security.reserved-names to this set
  private Set<String> reservedNames =
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
}
