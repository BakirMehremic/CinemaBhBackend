package com.atlantbh.cinemabh.dto.response;

public record VenueDetailsResponse(
    Long id,
    String name,
    String street,
    String streetNumber,
    String phone,
    String imageUrl,
    String cityName) {}
