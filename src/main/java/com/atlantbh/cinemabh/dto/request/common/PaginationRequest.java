package com.atlantbh.cinemabh.dto.request.common;

import com.atlantbh.cinemabh.constant.PaginationConstants;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class PaginationRequest {
  @Min(PaginationConstants.MIN_PAGE_NUMBER)
  private final Integer pageNumber;

  @Min(PaginationConstants.MIN_PAGE_SIZE)
  @Max(PaginationConstants.MAX_PAGE_SIZE)
  private final Integer pageSize;

  public PaginationRequest(Integer pageNumber, Integer pageSize) {
    this.pageNumber = pageNumber != null ? pageNumber : PaginationConstants.DEFAULT_PAGE_NUMBER;
    this.pageSize = pageSize != null ? pageSize : PaginationConstants.DEFAULT_PAGE_SIZE;
  }
}
