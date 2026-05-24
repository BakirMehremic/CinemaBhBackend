package com.atlantbh.cinemabh.dto.response;

import java.time.Instant;

public record ResendAtResponse<T>(T payload, Instant resendVerificationCodeAt) {}
