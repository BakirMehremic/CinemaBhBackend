package com.atlantbh.cinemabh.dto.request.venue;

import com.atlantbh.cinemabh.dto.request.common.PaginationRequest;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class FilterVenuesBasicInfo extends PaginationRequest {
  @Min(1)
  private final Long cityId;

  private final String name;

  public FilterVenuesBasicInfo(Integer pageNumber, Integer pageSize, Long cityId, String name) {
    super(pageNumber, pageSize);
    this.cityId = cityId;
    this.name = name;
  }
}
