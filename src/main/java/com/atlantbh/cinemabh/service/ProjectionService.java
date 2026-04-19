package com.atlantbh.cinemabh.service;

import java.time.LocalDate;
import java.util.List;

public interface ProjectionService {
  List<String> getShowingMoviesProjectionTimes(
      String movieName, Integer cityId, Integer venueId, Integer genreId, LocalDate date);
}
