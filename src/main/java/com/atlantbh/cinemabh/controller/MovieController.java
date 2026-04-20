package com.atlantbh.cinemabh.controller;

import com.atlantbh.cinemabh.dto.request.FilterShowingMoviesRequest;
import com.atlantbh.cinemabh.dto.response.MoviePreviewResponse;
import com.atlantbh.cinemabh.dto.response.MovieShowingResponse;
import com.atlantbh.cinemabh.dto.response.PaginatedResponse;
import com.atlantbh.cinemabh.enums.MovieShowingStatus;
import com.atlantbh.cinemabh.service.MovieService;
import com.atlantbh.cinemabh.validator.IdValidator;
import com.atlantbh.cinemabh.validator.PaginationValidator;
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
      @RequestParam(defaultValue = "SHOWING") MovieShowingStatus showingStatus,
      @RequestParam(defaultValue = "0") int pageNumber,
      @RequestParam(defaultValue = "4") int pageSize) {
    PaginationValidator.validate(pageNumber, pageSize);

    return ResponseEntity.ok(
        PaginatedResponse.from(
            movieService.getMoviesPreviewPaginated(pageNumber, pageSize, showingStatus)));
  }

  @GetMapping("/showing")
  public ResponseEntity<PaginatedResponse<MovieShowingResponse>> filterShowingMovies(
      @Valid FilterShowingMoviesRequest filter,
      @RequestParam(defaultValue = "0") int pageNumber,
      @RequestParam(defaultValue = "9") int pageSize) {
    PaginationValidator.validate(pageNumber, pageSize);

    return ResponseEntity.ok(
        PaginatedResponse.from(
            movieService.filterShowingMoviesPaginated(pageNumber, pageSize, filter)));
  }

  @GetMapping("/showing/venue")
  public ResponseEntity<PaginatedResponse<MoviePreviewResponse>> getMoviesByVenueIdPaginated(
      @RequestParam(defaultValue = "0") int pageNumber,
      @RequestParam(defaultValue = "9") int pageSize,
      @RequestParam long venueId) {
    PaginationValidator.validate(pageNumber, pageSize);
    IdValidator.validateIdPositive(venueId);

    return ResponseEntity.ok(
        PaginatedResponse.from(
            movieService.getMoviePreviewsPaginatedByVenueId(pageNumber, pageSize, venueId)));
  }
}
