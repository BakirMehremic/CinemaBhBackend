package com.atlantbh.cinemabh.dto.request.user;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

public record RegisterUserRequest(
    @NotBlank @Size(min = 1, max = 80) String firstName,
    @NotBlank @Size(min = 1, max = 50) String lastName,
    @NotBlank @Email String email,
    @NotBlank @Size(min = 8, max = 48) String password,
    @NotBlank @URL String imageUrl,
    @NotBlank @Size(min = 7, max = 15) String phoneNumber,
    @NotNull @Min(1) Long cityId,
    @NotBlank @Size(max = 50) String street) {}
