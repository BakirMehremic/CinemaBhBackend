package com.atlantbh.cinemabh.dto.response;

public record UserPreviewResponse(
    Long id,
    String firstName,
    String lastName,
    String email,
    String phoneNumber,
    String imageUrl) {}
