package com.atlantbh.cinemabh.dto.request.user;

import static com.atlantbh.cinemabh.constant.AuthConstants.*;
import static com.atlantbh.cinemabh.constant.UserConstants.EMAIL_MAX_LENGTH;
import static com.atlantbh.cinemabh.constant.UserConstants.EMAIL_MIN_LENGTH;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PasswordResetConfirmRequest(
    @NotBlank @Email @Size(min = EMAIL_MIN_LENGTH, max = EMAIL_MAX_LENGTH) String email,
    @NotBlank @Pattern(regexp = "^\\d{6}$", message = "Verification code must be exactly 6 digits")
        String verificationCode,
    @NotBlank @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH) String newPassword) {}
