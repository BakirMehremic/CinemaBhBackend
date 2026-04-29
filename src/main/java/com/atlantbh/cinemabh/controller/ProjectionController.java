package com.atlantbh.cinemabh.controller;

import com.atlantbh.cinemabh.dto.request.projection.FilterProjectionTimesRequest;
import com.atlantbh.cinemabh.service.ProjectionService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projections")
public class ProjectionController {
  private final ProjectionService projectionService;

  @GetMapping("/showing/times")
  public ResponseEntity<List<String>> getShowingMoviesProjectionTimes(
      @Valid FilterProjectionTimesRequest request) {

    return ResponseEntity.ok(projectionService.getShowingMoviesProjectionTimes(request));
  }
}
