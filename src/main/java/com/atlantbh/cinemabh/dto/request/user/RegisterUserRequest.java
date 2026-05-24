package com.atlantbh.cinemabh.dto.request.user;

import static com.atlantbh.cinemabh.constant.AuthConstants.*;
import static com.atlantbh.cinemabh.constant.UserConstants.*;

import jakarta.validation.constraints.*;

public record RegisterUserRequest(
    @NotBlank @Size(min = 1, max = FIRST_NAME_MAX_LENGTH) String firstName,
    @NotBlank @Size(min = 1, max = LAST_NAME_MAX_LENGTH) String lastName,
    @NotBlank @Email @Size(min = EMAIL_MIN_LENGTH, max = EMAIL_MAX_LENGTH) String email,
    @NotBlank @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH) String password) {}
