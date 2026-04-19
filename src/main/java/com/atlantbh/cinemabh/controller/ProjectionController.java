package com.atlantbh.cinemabh.controller;

import com.atlantbh.cinemabh.service.ProjectionService;
import com.atlantbh.cinemabh.validator.IdValidator;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/projections")
public class ProjectionController {
  private final ProjectionService projectionService;

  @GetMapping("/showing/times")
  public ResponseEntity<List<String>> getShowingMoviesProjectionTimes(
      @RequestParam(required = false) String movieName,
      @RequestParam(required = false) Integer cityId,
      @RequestParam(required = false) Integer venueId,
      @RequestParam(required = false) Integer genreId,
      @RequestParam(required = false) LocalDate date) {
    IdValidator.validateIdNullOrPositive(cityId, venueId, genreId);

    List<String> projectionTimes =
        projectionService.getShowingMoviesProjectionTimes(
            movieName, cityId, venueId, genreId, date);

    return ResponseEntity.ok(projectionTimes);
  }
}
