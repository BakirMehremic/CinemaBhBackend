package com.atlantbh.cinemabh.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record MovieShowingResponse(
    Long id,
    String name,
    String coverPhotoUrl,
    String pgRating,
    String language,
    Integer duration,
    List<String> genres,
    List<LocalTime> projectionTimes,
    LocalDate lastProjectionDate) {}
