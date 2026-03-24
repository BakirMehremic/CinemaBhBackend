package com.atlantbh.cinemabh.controller;

import com.atlantbh.cinemabh.dto.response.MoviePreviewResponse;
import com.atlantbh.cinemabh.dto.response.PaginatedResponse;
import com.atlantbh.cinemabh.enums.MovieShowingStatus;
import com.atlantbh.cinemabh.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/movies")
public class MovieController {
  private final MovieService movieService;

  @GetMapping("/preview")
  public ResponseEntity<PaginatedResponse<MoviePreviewResponse>> getMoviesPreview(
      @RequestParam(defaultValue = "SHOWING") MovieShowingStatus showingStatus,
      @RequestParam(defaultValue = "0") int pageNumber,
      @RequestParam(defaultValue = "4") int pageSize) {

    Page<MoviePreviewResponse> movies =
        movieService.getMoviesPreviewPaginated(pageNumber, pageSize, showingStatus);

    PaginatedResponse<MoviePreviewResponse> response = PaginatedResponse.from(movies);

    return ResponseEntity.ok(response);
  }
}
