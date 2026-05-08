package com.atlantbh.cinemabh.dto.response;

public record AuthResponse(String accessToken, String refreshToken, UserPreviewResponse user) {}
