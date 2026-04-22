package com.atlantbh.cinemabh.dto.request;

import jakarta.validation.constraints.Min;

public record PaginationRequest(@Min(0) int pageNumber, @Min(1) int pageSize) {}
