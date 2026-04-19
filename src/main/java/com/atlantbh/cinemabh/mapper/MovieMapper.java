package com.atlantbh.cinemabh.mapper;

import com.atlantbh.cinemabh.dto.response.MoviePreviewResponse;
import com.atlantbh.cinemabh.dto.response.MovieShowingResponse;
import com.atlantbh.cinemabh.entity.Movie;
import com.atlantbh.cinemabh.projection.MovieShowingProjection;
import java.util.List;
import org.springframework.data.domain.Page;

public interface MovieMapper {
  MoviePreviewResponse toPreviewResponse(Movie movie);

  List<MoviePreviewResponse> toPreviewResponseList(List<Long> ids, List<Movie> movies);

  MovieShowingResponse toShowingResponse(Movie movie);

  List<MovieShowingResponse> toShowingResponseList(List<Long> ids, List<Movie> movies);

  Page<MovieShowingResponse> toShowingResponseList(Page<MovieShowingProjection> projections);
}
