package com.atlantbh.cinemabh.dto.request.movie;

import com.atlantbh.cinemabh.dto.request.common.PaginationRequest;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;

@Getter
public class FilterShowingMoviesRequest extends PaginationRequest {
  @NotNull @FutureOrPresent private final LocalDate projectionDate;
  private final LocalTime projectionTime;
  private final String name;

  @Min(1)
  private final Long cityId;

  @Min(1)
  private final Long venueId;

  @Min(1)
  private final Long genreId;

  public FilterShowingMoviesRequest(
      Integer pageNumber,
      Integer pageSize,
      LocalDate projectionDate,
      LocalTime projectionTime,
      String name,
      Long cityId,
      Long venueId,
      Long genreId) {
    super(pageNumber, pageSize);
    this.projectionDate = projectionDate;
    this.projectionTime = projectionTime;
    this.name = name;
    this.cityId = cityId;
    this.venueId = venueId;
    this.genreId = genreId;
  }
}
