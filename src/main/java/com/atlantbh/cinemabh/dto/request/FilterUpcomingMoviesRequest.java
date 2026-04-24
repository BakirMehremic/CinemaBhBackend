package com.atlantbh.cinemabh.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import lombok.Getter;

@Getter
public class FilterUpcomingMoviesRequest extends PaginationRequest {
  @Future private final LocalDate startShowingDateFrom;

  @Future private final LocalDate startShowingDateTo;

  private final String name;

  @Min(1)
  private final Long cityId;

  @Min(1)
  private final Long venueId;

  @Min(1)
  private final Long genreId;

  public FilterUpcomingMoviesRequest(
      int pageNumber,
      int pageSize,
      LocalDate startShowingDateFrom,
      LocalDate startShowingDateTo,
      String name,
      Long cityId,
      Long venueId,
      Long genreId) {
    super(pageNumber, pageSize);
    this.startShowingDateFrom = startShowingDateFrom;
    this.startShowingDateTo = startShowingDateTo;
    this.name = name;
    this.cityId = cityId;
    this.venueId = venueId;
    this.genreId = genreId;
  }
}
