package com.atlantbh.cinemabh.service.impl;

import com.atlantbh.cinemabh.dto.request.projection.FilterProjectionTimesRequest;
import com.atlantbh.cinemabh.repository.ProjectionRepository;
import com.atlantbh.cinemabh.service.ProjectionService;
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
  public List<String> getShowingMoviesProjectionTimes(FilterProjectionTimesRequest filter) {
    List<LocalTime> projectionTimes =
        projectionRepository.getShowingMoviesProjectionTimes(
            filter.getMovieName(),
            filter.getCityId(),
            filter.getVenueId(),
            filter.getGenreId(),
            filter.getDate());

    return projectionTimes.stream().map(LocalTime::toString).toList();
  }
}
