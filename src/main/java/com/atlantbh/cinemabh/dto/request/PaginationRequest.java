package com.atlantbh.cinemabh.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaginationRequest {
  @Min(0)
  @NotNull
  private final int pageNumber;

  @Min(1)
  @Max(100)
  @NotNull
  private final int pageSize;
}
