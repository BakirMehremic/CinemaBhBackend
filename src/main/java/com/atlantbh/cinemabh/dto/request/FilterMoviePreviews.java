package com.atlantbh.cinemabh.dto.request;

import com.atlantbh.cinemabh.constant.MovieFilterConstants;
import com.atlantbh.cinemabh.enums.MovieShowingStatus;
import lombok.Getter;

@Getter
public class FilterMoviePreviews extends PaginationRequest {
  private final MovieShowingStatus showingStatus;

  public FilterMoviePreviews(
      Integer pageNumber, Integer pageSize, MovieShowingStatus showingStatus) {
    super(pageNumber, pageSize);
    this.showingStatus =
        showingStatus != null ? showingStatus : MovieFilterConstants.DEFAULT_SHOWING_STATUS;
  }
}
