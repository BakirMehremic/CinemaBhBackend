package com.atlantbh.cinemabh.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;

public record FilterUpcomingMoviesRequest(
    @Future LocalDate startShowingDateFrom,
    @Future LocalDate startShowingDateTo,
    String name,
    @Min(1) Long cityId,
    @Min(1) Long venueId,
    @Min(1) Long genreId) {}
