package com.atlantbh.cinemabh.controller;

import com.atlantbh.cinemabh.dto.response.MoviePreviewResponse;
import com.atlantbh.cinemabh.dto.response.PaginatedResponse;
import com.atlantbh.cinemabh.enums.MovieShowingStatus;
import com.atlantbh.cinemabh.service.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/movies")
public class MovieController {
  private final MovieService movieService;

  public MovieController(MovieService movieService) {
    this.movieService = movieService;
  }

  @GetMapping("/preview")
  public ResponseEntity<PaginatedResponse<MoviePreviewResponse>> getMoviesPreview(
      @RequestParam(defaultValue = "SHOWING") MovieShowingStatus showingStatus,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "4") int size) {

    Page<MoviePreviewResponse> movies =
        movieService.getMoviesPreviewPaginated(page, size, showingStatus);

    PaginatedResponse<MoviePreviewResponse> response = PaginatedResponse.from(movies);

    return ResponseEntity.ok(response);
  }
}
