package com.atlantbh.cinemabh.service.impl;

import com.atlantbh.cinemabh.repository.ProjectionRepository;
import com.atlantbh.cinemabh.service.ProjectionService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProjectionServiceImpl implements ProjectionService {
  private final ProjectionRepository projectionRepository;

  @Override
  @Transactional(readOnly = true)
  public List<String> getShowingMoviesProjectionTimes(
      String movieName, Long cityId, Long venueId, Long genreId, LocalDate date) {
    List<LocalTime> projectionTimes =
        projectionRepository.getShowingMoviesProjectionTimes(
            movieName, cityId, venueId, genreId, date);

    return projectionTimes.stream().map(LocalTime::toString).toList();
  }
}
