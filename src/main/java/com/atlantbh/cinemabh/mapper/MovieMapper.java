package com.atlantbh.cinemabh.mapper;

import com.atlantbh.cinemabh.dto.response.MoviePreviewResponse;
import com.atlantbh.cinemabh.entity.Movie;

public interface MovieMapper {
  public MoviePreviewResponse toPreviewResponse(Movie movie);
}
