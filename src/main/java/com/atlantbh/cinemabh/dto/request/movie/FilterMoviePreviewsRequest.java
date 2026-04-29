package com.atlantbh.cinemabh.dto.request.movie;

import com.atlantbh.cinemabh.constant.MovieFilterConstants;
import com.atlantbh.cinemabh.dto.request.common.PaginationRequest;
import com.atlantbh.cinemabh.enums.MovieShowingStatus;
import lombok.Getter;

@Getter
public class FilterMoviePreviewsRequest extends PaginationRequest {
  private final MovieShowingStatus showingStatus;

  public FilterMoviePreviewsRequest(
      Integer pageNumber, Integer pageSize, MovieShowingStatus showingStatus) {
    super(pageNumber, pageSize);
    this.showingStatus =
        showingStatus != null ? showingStatus : MovieFilterConstants.DEFAULT_SHOWING_STATUS;
  }
}
