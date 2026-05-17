package com.atlantbh.cinemabh.dto.response;

public record UserDetailsResponse(
    Long id,
    String firstName,
    String lastName,
    String email,
    String phoneNumber,
    String imageUrl,
    String role,
    boolean verified) {}
