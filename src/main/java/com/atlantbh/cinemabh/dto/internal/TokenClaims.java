package com.atlantbh.cinemabh.dto.internal;

import com.atlantbh.cinemabh.enums.UserRole;
import java.time.Instant;

public record TokenClaims(long userId, UserRole role, Instant expiration) {}
