package com.atlantbh.cinemabh.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record FilterUpcomingMoviesRequest(
    @Future LocalDate startShowingDateFrom,
    @Future LocalDate startShowingDateTo,
    String name,
    @Min(1) Long cityId,
    @Min(1) Long venueId,
    @Min(1) Long genreId,
    @Min(0) @NotNull Integer pageNumber,
    @Min(1) @NotNull Integer pageSize) {}
