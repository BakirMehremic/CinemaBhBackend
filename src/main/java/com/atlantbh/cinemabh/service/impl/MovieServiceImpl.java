package com.atlantbh.cinemabh.service.impl;

import com.atlantbh.cinemabh.dto.request.FilterShowingMoviesRequest;
import com.atlantbh.cinemabh.dto.response.MoviePreviewResponse;
import com.atlantbh.cinemabh.dto.response.MovieShowingResponse;
import com.atlantbh.cinemabh.entity.Movie;
import com.atlantbh.cinemabh.enums.MovieShowingStatus;
import com.atlantbh.cinemabh.mapper.MovieMapper;
import com.atlantbh.cinemabh.repository.MovieRepository;
import com.atlantbh.cinemabh.service.MovieService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MovieServiceImpl implements MovieService {
  private final MovieRepository movieRepository;
  private final MovieMapper movieMapper;

  private final Logger logger = LoggerFactory.getLogger(MovieServiceImpl.class);

  @Override
  @Transactional(readOnly = true)
  public Page<MoviePreviewResponse> getMoviesPreviewPaginated(
      int pageNumber, int pageSize, MovieShowingStatus showingStatus) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);

    Page<Long> pagedIds =
        switch (showingStatus) {
          case UPCOMING -> movieRepository.getUpcomingMovieIdsPaginated(pageable);
          case SHOWING -> movieRepository.getShowingMovieIdsPaginated(pageable);
        };

    if (pagedIds.isEmpty()) {
      return new PageImpl<>(List.of(), pageable, pagedIds.getTotalElements());
    }

    List<Movie> movies = movieRepository.getMoviesWithGenresAndPhotosByIds(pagedIds.getContent());

    List<MoviePreviewResponse> content =
        movieMapper.toPreviewResponseList(pagedIds.getContent(), movies);

    return new PageImpl<>(content, pageable, pagedIds.getTotalElements());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<MovieShowingResponse> filterShowingMoviesPaginated(
      int pageNumber, int pageSize, FilterShowingMoviesRequest filter) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    logger.warn(filter.toString());

    Page<Long> pagedIds =
        movieRepository.getShowingMovieIdsFilteredPaginated(
            pageable,
            filter.projectionDate(),
            filter.projectionTime(),
            filter.name(),
            filter.cityId(),
            filter.venueId(),
            filter.genreId());

    if (pagedIds.isEmpty()) {
      logger.warn(pagedIds.toString());
      return new PageImpl<>(List.of(), pageable, pagedIds.getTotalElements());
    }

    List<Movie> movies =
        movieRepository.getMoviesWithGenresProjectionsPhotosByIds(pagedIds.getContent());

    List<MovieShowingResponse> content =
        movieMapper.toShowingResponseList(pagedIds.getContent(), movies);

    return new PageImpl<>(content, pageable, pagedIds.getTotalElements());
  }
}
