package com.atlantbh.cinemabh.service;

import java.time.LocalDate;
import java.util.List;

public interface ProjectionService {
  List<String> getShowingMoviesProjectionTimes(
      String movieName, Long cityId, Long venueId, Long genreId, LocalDate date);
}
