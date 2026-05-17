package com.atlantbh.cinemabh.dto.internal;

import java.util.Date;

public record TokenClaims(long userId, String role, Date expiration) {}
