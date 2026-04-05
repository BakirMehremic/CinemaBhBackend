package com.atlantbh.cinemabh.service;

import com.atlantbh.cinemabh.dto.request.FilterShowingMoviesRequest;
import com.atlantbh.cinemabh.dto.response.MoviePreviewResponse;
import com.atlantbh.cinemabh.dto.response.MovieShowingResponse;
import com.atlantbh.cinemabh.enums.MovieShowingStatus;
import org.springframework.data.domain.Page;

public interface MovieService {
  Page<MoviePreviewResponse> getMoviesPreviewPaginated(
      int pageNumber, int pageSize, MovieShowingStatus showingStatus);

  Page<MovieShowingResponse> filterShowingMoviesPaginated(
      int pageNumber, int pageSize, FilterShowingMoviesRequest filter);
}
