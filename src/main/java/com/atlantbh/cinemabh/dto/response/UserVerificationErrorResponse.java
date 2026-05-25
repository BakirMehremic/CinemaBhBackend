package com.atlantbh.cinemabh.dto.response;

import java.time.Instant;

public record UserVerificationErrorResponse(String message, Instant resend_verification_code_at) {}
