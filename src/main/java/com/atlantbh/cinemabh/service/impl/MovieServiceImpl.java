package com.atlantbh.cinemabh.service.impl;

import com.atlantbh.cinemabh.dto.request.FilterShowingMoviesRequest;
import com.atlantbh.cinemabh.dto.response.MoviePreviewResponse;
import com.atlantbh.cinemabh.dto.response.MovieShowingResponse;
import com.atlantbh.cinemabh.entity.Movie;
import com.atlantbh.cinemabh.enums.MovieShowingStatus;
import com.atlantbh.cinemabh.mapper.MovieMapper;
import com.atlantbh.cinemabh.projection.MovieShowingProjection;
import com.atlantbh.cinemabh.repository.MovieRepository;
import com.atlantbh.cinemabh.service.MovieService;
import java.util.List;
import lombok.RequiredArgsConstructor;
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
    Page<MovieShowingProjection> projections =
        movieRepository.filterShowingMoviesPaginated(
            PageRequest.of(pageNumber, pageSize),
            filter.projectionDate(),
            filter.projectionTime(),
            filter.name(),
            filter.cityId(),
            filter.venueId(),
            filter.genreId());

    return movieMapper.toShowingResponseList(projections);
  }

  @Override
  public Page<MoviePreviewResponse> getMoviePreviewsPaginatedByVenueId(
      int pageNumber, int pageSize, int venueId) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);

    Page<Movie> movies = movieRepository.getMoviesShowingPreviewsByVenueId(pageable, venueId);

    return movies.map(movieMapper::toPreviewResponse);
  }
}
