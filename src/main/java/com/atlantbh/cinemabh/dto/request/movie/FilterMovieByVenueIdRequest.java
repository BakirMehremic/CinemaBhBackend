package com.atlantbh.cinemabh.dto.request.movie;

import com.atlantbh.cinemabh.dto.request.common.PaginationRequest;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class FilterMovieByVenueIdRequest extends PaginationRequest {
  @Min(1)
  private final long venueId;

  public FilterMovieByVenueIdRequest(Integer pageNumber, Integer pageSize, long venueId) {
    super(pageNumber, pageSize);
    this.venueId = venueId;
  }
}
