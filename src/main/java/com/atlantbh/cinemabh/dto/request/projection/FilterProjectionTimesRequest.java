package com.atlantbh.cinemabh.dto.request.projection;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FilterProjectionTimesRequest {
  private final String movieName;

  @Min(1)
  private final Long cityId;

  @Min(1)
  private final Long venueId;

  @Min(1)
  private final Long genreId;

  @FutureOrPresent private final LocalDate date;
}
