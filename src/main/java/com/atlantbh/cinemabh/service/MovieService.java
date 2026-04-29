package com.atlantbh.cinemabh.service;

import com.atlantbh.cinemabh.dto.request.FilterMoviePreviews;
import com.atlantbh.cinemabh.dto.request.FilterShowingMoviesRequest;
import com.atlantbh.cinemabh.dto.request.FilterUpcomingMoviesRequest;
import com.atlantbh.cinemabh.dto.response.MoviePreviewResponse;
import com.atlantbh.cinemabh.dto.response.MovieShowingResponse;
import com.atlantbh.cinemabh.projection.MovieUpcomingProjection;
import org.springframework.data.domain.Page;

public interface MovieService {
  Page<MoviePreviewResponse> getMoviesPreviewPaginated(FilterMoviePreviews filter);

  Page<MovieShowingResponse> filterShowingMoviesPaginated(
      int pageNumber, int pageSize, FilterShowingMoviesRequest filter);

  Page<MoviePreviewResponse> getMoviePreviewsPaginatedByVenueId(
      int pageNumber, int pageSize, long venueId);

  Page<MovieUpcomingProjection> filterUpcomingMoviesPaginated(FilterUpcomingMoviesRequest filter);
}
