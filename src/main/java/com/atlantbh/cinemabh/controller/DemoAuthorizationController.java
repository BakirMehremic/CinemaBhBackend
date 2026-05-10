package com.atlantbh.cinemabh.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO delete after pr - demo only
@RestController
@RequestMapping("/demo")
public class DemoAuthorizationController {

  @GetMapping("/public")
  public String publicEndpoint() {
    return "This is public";
  }

  @GetMapping("/user")
  @PreAuthorize("hasRole('REGISTERED_USER') or hasRole('ADMIN')")
  public String userEndpoint() {
    return "Accessible by REGISTERED_USER and ADMIN";
  }

  @GetMapping("/admin")
  @PreAuthorize("hasRole('ADMIN')")
  public String adminEndpoint() {
    return "Only ADMIN can access this";
  }
}
