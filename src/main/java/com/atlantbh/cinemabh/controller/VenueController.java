package com.atlantbh.cinemabh.controller;

import com.atlantbh.cinemabh.dto.request.common.PaginationRequest;
import com.atlantbh.cinemabh.dto.request.venue.FilterVenuesBasicInfo;
import com.atlantbh.cinemabh.dto.response.*;
import com.atlantbh.cinemabh.projection.VenueBasicInfoProjection;
import com.atlantbh.cinemabh.projection.VenueDetailsProjection;
import com.atlantbh.cinemabh.service.VenueService;
import com.atlantbh.cinemabh.validator.IdValidator;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/venues")
public class VenueController {
  private final VenueService venueService;

  @GetMapping("/preview")
  public ResponseEntity<PaginatedResponse<VenuePreviewResponse>> getVenuesPreview(
      @Valid PaginationRequest request) {

    return ResponseEntity.ok(
        PaginatedResponse.from(venueService.getVenuePreviewsPaginated(request)));
  }

  @GetMapping("/names")
  public ResponseEntity<List<NameIdPair>> getVenuesNameIdPairs(
      @RequestParam(required = false) Long cityId) {
    IdValidator.validateIdNullOrPositive(cityId);

    return ResponseEntity.ok(venueService.getAllVenueNameIdPairs(cityId));
  }

  @GetMapping("/basic")
  public ResponseEntity<PaginatedResponse<VenueBasicInfoProjection>> getVenuesBasicInfo(
      @Valid FilterVenuesBasicInfo request) {

    return ResponseEntity.ok(
        PaginatedResponse.from(venueService.getVenuesBasicInfoPaginated(request)));
  }

  @GetMapping("/details/{venueId}")
  public ResponseEntity<VenueDetailsProjection> getVenueDetailsById(@PathVariable long venueId) {
    IdValidator.validateIdPositive(venueId);

    return ResponseEntity.ok(venueService.getVenueDetailsById(venueId));
  }
}
