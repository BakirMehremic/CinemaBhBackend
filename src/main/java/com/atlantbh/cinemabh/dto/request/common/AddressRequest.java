package com.atlantbh.cinemabh.dto.request.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record AddressRequest(
    @NotNull @Positive Long cityId, @NotBlank @Size(max = 150) String street) {}
