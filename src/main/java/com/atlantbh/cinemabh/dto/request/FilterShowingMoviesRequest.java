package com.atlantbh.cinemabh.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record FilterShowingMoviesRequest(
    @NotNull @FutureOrPresent LocalDate projectionDate,
    LocalTime projectionTime,
    String name,
    @Min(1) Long cityId,
    @Min(1) Long venueId,
    @Min(1) Long genreId) {}
