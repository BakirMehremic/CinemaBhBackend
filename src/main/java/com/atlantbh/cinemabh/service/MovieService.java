package com.atlantbh.cinemabh.service;

import com.atlantbh.cinemabh.dto.request.movie.FilterMovieByVenueIdRequest;
import com.atlantbh.cinemabh.dto.request.movie.FilterMoviePreviewsRequest;
import com.atlantbh.cinemabh.dto.request.movie.FilterShowingMoviesRequest;
import com.atlantbh.cinemabh.dto.request.movie.FilterUpcomingMoviesRequest;
import com.atlantbh.cinemabh.dto.response.MoviePreviewResponse;
import com.atlantbh.cinemabh.dto.response.MovieShowingResponse;
import com.atlantbh.cinemabh.projection.MovieDetailsProjection;
import com.atlantbh.cinemabh.projection.MovieShowingProjection;
import com.atlantbh.cinemabh.projection.MovieUpcomingProjection;
import org.springframework.data.domain.Page;

public interface MovieService {
  Page<MoviePreviewResponse> getMoviesPreviewPaginated(FilterMoviePreviewsRequest filter);

  Page<MovieShowingResponse> filterShowingMoviesPaginated(FilterShowingMoviesRequest filter);

  Page<MovieShowingProjection> getMoviePreviewsPaginatedByVenueId(
      FilterMovieByVenueIdRequest filter);

  Page<MovieUpcomingProjection> filterUpcomingMoviesPaginated(FilterUpcomingMoviesRequest filter);

  MovieDetailsProjection getMovieDetailsById(long movieId);
}
