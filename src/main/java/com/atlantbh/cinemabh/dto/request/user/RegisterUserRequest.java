package com.atlantbh.cinemabh.dto.request.user;

import static com.atlantbh.cinemabh.constant.AuthConstants.*;
import static com.atlantbh.cinemabh.constant.UserConstants.*;

import com.atlantbh.cinemabh.dto.request.common.AddressRequest;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;

public record RegisterUserRequest(
    @NotBlank @Size(min = 1, max = FIRST_NAME_MAX_LENGTH) String firstName,
    @NotBlank @Size(min = 1, max = LAST_NAME_MAX_LENGTH) String lastName,
    @NotBlank @Email @Size(min = EMAIL_MIN_LENGTH, max = EMAIL_MAX_LENGTH) String email,
    @NotBlank @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH) String password,
    @NotBlank @URL String imageUrl,
    @NotBlank @Size(min = PHONE_NUMBER_MIN_LENGTH, max = PHONE_NUMBER_MAX_LENGTH)
        String phoneNumber,
    @NotNull AddressRequest address) {}
