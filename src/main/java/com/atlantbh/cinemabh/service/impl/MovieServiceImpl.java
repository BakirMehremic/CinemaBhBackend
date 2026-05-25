package com.atlantbh.cinemabh.service.impl;

import com.atlantbh.cinemabh.dto.request.movie.FilterMovieByVenueIdRequest;
import com.atlantbh.cinemabh.dto.request.movie.FilterMoviePreviewsRequest;
import com.atlantbh.cinemabh.dto.request.movie.FilterShowingMoviesRequest;
import com.atlantbh.cinemabh.dto.request.movie.FilterUpcomingMoviesRequest;
import com.atlantbh.cinemabh.dto.response.MoviePreviewResponse;
import com.atlantbh.cinemabh.dto.response.MovieShowingResponse;
import com.atlantbh.cinemabh.entity.Movie;
import com.atlantbh.cinemabh.exception.NotFoundException;
import com.atlantbh.cinemabh.mapper.MovieMapper;
import com.atlantbh.cinemabh.projection.MovieDetailsProjection;
import com.atlantbh.cinemabh.projection.MoviePreviewProjection;
import com.atlantbh.cinemabh.projection.MovieShowingProjection;
import com.atlantbh.cinemabh.projection.MovieUpcomingProjection;
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
  public Page<MoviePreviewResponse> getMoviesPreviewPaginated(FilterMoviePreviewsRequest filter) {
    Pageable pageable = PageRequest.of(filter.getPageNumber(), filter.getPageSize());

    Page<Long> pagedIds =
        switch (filter.getShowingStatus()) {
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
      FilterShowingMoviesRequest filter) {
    Page<MovieShowingProjection> projections =
        movieRepository.filterShowingMoviesPaginated(
            PageRequest.of(filter.getPageNumber(), filter.getPageSize()),
            filter.getProjectionDate(),
            filter.getProjectionTime(),
            filter.getName(),
            filter.getCityId(),
            filter.getVenueId(),
            filter.getGenreId());

    return movieMapper.toShowingResponseList(projections);
  }

  @Override
  public Page<MoviePreviewProjection> getMoviePreviewsPaginatedByVenueId(
      FilterMovieByVenueIdRequest filter) {
    Pageable pageable = PageRequest.of(filter.getPageNumber(), filter.getPageSize());

    return movieRepository.getMoviesShowingPreviewsByVenueId(pageable, filter.getVenueId());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<MovieUpcomingProjection> filterUpcomingMoviesPaginated(
      FilterUpcomingMoviesRequest filter) {
    return movieRepository.filterUpcomingMoviesPaginated(
        PageRequest.of(filter.getPageNumber(), filter.getPageSize()),
        filter.getStartShowingDateFrom(),
        filter.getStartShowingDateTo(),
        filter.getName(),
        filter.getCityId(),
        filter.getVenueId(),
        filter.getGenreId());
  }

  @Override
  @Transactional(readOnly = true)
  public MovieDetailsProjection getMovieDetailsById(long movieId) {
    return movieRepository
        .getMovieDetailsById(movieId)
        .orElseThrow(() -> new NotFoundException("Movie with id " + movieId + " not found"));
  }
}
