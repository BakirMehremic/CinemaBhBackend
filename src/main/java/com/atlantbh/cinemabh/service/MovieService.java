package com.atlantbh.cinemabh.service;

import com.atlantbh.cinemabh.dto.response.MoviePreviewResponse;
import com.atlantbh.cinemabh.enums.MovieShowingStatus;
import org.springframework.data.domain.Page;

public interface MovieService {
  public Page<MoviePreviewResponse> getMoviesPreviewPaginated(
      Integer pageNumber, Integer PageSize, MovieShowingStatus showingStatus);
}
