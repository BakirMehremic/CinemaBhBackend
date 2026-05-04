package com.atlantbh.cinemabh.controller;

import com.atlantbh.cinemabh.dto.request.movie.FilterMovieByVenueIdRequest;
import com.atlantbh.cinemabh.dto.request.movie.FilterMoviePreviewsRequest;
import com.atlantbh.cinemabh.dto.request.movie.FilterShowingMoviesRequest;
import com.atlantbh.cinemabh.dto.request.movie.FilterUpcomingMoviesRequest;
import com.atlantbh.cinemabh.dto.response.MoviePreviewResponse;
import com.atlantbh.cinemabh.dto.response.MovieShowingResponse;
import com.atlantbh.cinemabh.dto.response.PaginatedResponse;
import com.atlantbh.cinemabh.projection.MovieUpcomingProjection;
import com.atlantbh.cinemabh.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/movies")
public class MovieController {
  private final MovieService movieService;

  @GetMapping("/preview")
  public ResponseEntity<PaginatedResponse<MoviePreviewResponse>> getMoviesPreview(
      @Valid FilterMoviePreviewsRequest request) {

    return ResponseEntity.ok(
        PaginatedResponse.from(movieService.getMoviesPreviewPaginated(request)));
  }

  @GetMapping("/showing")
  public ResponseEntity<PaginatedResponse<MovieShowingResponse>> filterShowingMovies(
      @Valid FilterShowingMoviesRequest filter) {

    return ResponseEntity.ok(
        PaginatedResponse.from(movieService.filterShowingMoviesPaginated(filter)));
  }

  @GetMapping("/showing/venue")
  public ResponseEntity<PaginatedResponse<MoviePreviewResponse>> getMoviesByVenueIdPaginated(
      @Valid FilterMovieByVenueIdRequest request) {

    return ResponseEntity.ok(
        PaginatedResponse.from(movieService.getMoviePreviewsPaginatedByVenueId(request)));
  }

  @GetMapping("/upcoming")
  public ResponseEntity<PaginatedResponse<MovieUpcomingProjection>> filterUpcomingMoviesPaginated(
      @Valid FilterUpcomingMoviesRequest request) {

    return ResponseEntity.ok(
        PaginatedResponse.from(movieService.filterUpcomingMoviesPaginated(request)));
  }
}
