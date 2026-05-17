package com.atlantbh.cinemabh.dto.response;

// TODO create class representing token
public record AuthResponse(String accessToken, String refreshToken, UserDetailsResponse user) {}
