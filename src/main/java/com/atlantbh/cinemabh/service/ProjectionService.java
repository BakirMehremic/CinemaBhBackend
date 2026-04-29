package com.atlantbh.cinemabh.service;

import com.atlantbh.cinemabh.dto.request.projection.FilterProjectionTimesRequest;
import java.util.List;

public interface ProjectionService {
  List<String> getShowingMoviesProjectionTimes(FilterProjectionTimesRequest filter);
}
