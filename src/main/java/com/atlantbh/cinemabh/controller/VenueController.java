package com.atlantbh.cinemabh.controller;

import com.atlantbh.cinemabh.dto.response.PaginatedResponse;
import com.atlantbh.cinemabh.dto.response.VenuePreviewResponse;
import com.atlantbh.cinemabh.service.VenueService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/venues")
public class VenueController {
  private final VenueService venueService;

  public VenueController(VenueService venueService) {
    this.venueService = venueService;
  }

  @GetMapping("/preview")
  public ResponseEntity<PaginatedResponse<VenuePreviewResponse>> getVenuesPreview(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "4") int size) {

    Page<VenuePreviewResponse> venues = venueService.getVenuePreviewsPaginated(page, size);

    PaginatedResponse<VenuePreviewResponse> response = PaginatedResponse.from(venues);

    return ResponseEntity.ok(response);
  }
}
