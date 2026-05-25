package com.atlantbh.cinemabh.dto.request.user;

import static com.atlantbh.cinemabh.constant.AuthConstants.*;
import static com.atlantbh.cinemabh.constant.UserConstants.EMAIL_MAX_LENGTH;
import static com.atlantbh.cinemabh.constant.UserConstants.EMAIL_MIN_LENGTH;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VerificationRequest(
    @NotBlank @Email @Size(min = EMAIL_MIN_LENGTH, max = EMAIL_MAX_LENGTH) String email,
    @NotBlank @Size(min = VERIFICATION_CODE_LENGTH, max = VERIFICATION_CODE_LENGTH)
        String verificationCode) {}
