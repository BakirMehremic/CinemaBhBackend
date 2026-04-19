package com.atlantbh.cinemabh.controller;

import com.atlantbh.cinemabh.dto.response.*;
import com.atlantbh.cinemabh.projection.VenueBasicInfoProjection;
import com.atlantbh.cinemabh.projection.VenueDetailsProjection;
import com.atlantbh.cinemabh.service.VenueService;
import com.atlantbh.cinemabh.validator.IdValidator;
import com.atlantbh.cinemabh.validator.PaginationValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/venues")
public class VenueController {
  private final VenueService venueService;

  @GetMapping("/preview")
  public ResponseEntity<PaginatedResponse<VenuePreviewResponse>> getVenuesPreview(
      @RequestParam(defaultValue = "0") int pageNumber,
      @RequestParam(defaultValue = "4") int pageSize) {
    PaginationValidator.validate(pageNumber, pageSize);

    Page<VenuePreviewResponse> venues =
        venueService.getVenuePreviewsPaginated(pageNumber, pageSize);

    PaginatedResponse<VenuePreviewResponse> response = PaginatedResponse.from(venues);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/names")
  public ResponseEntity<List<NameIdPair>> getVenuesNameIdPairs(
      @RequestParam(required = false) Integer cityId) {
    List<NameIdPair> response = venueService.getAllVenueNameIdPairs(cityId);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/basic")
  public ResponseEntity<PaginatedResponse<VenueBasicInfoProjection>> getVenuesBasicInfo(
      @RequestParam(defaultValue = "0") int pageNumber,
      @RequestParam(defaultValue = "4") int pageSize,
      @RequestParam(required = false) Integer cityId,
      @RequestParam(required = false) String name) {
    PaginationValidator.validate(pageNumber, pageSize);
    IdValidator.validateIdNullOrPositive(cityId);

    Page<VenueBasicInfoProjection> venues =
        venueService.getVenuesBasicInfoPaginated(pageNumber, pageSize, cityId, name);

    PaginatedResponse<VenueBasicInfoProjection> response = PaginatedResponse.from(venues);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/details/{venueId}")
  public ResponseEntity<VenueDetailsProjection> getVenueDetailsById(@PathVariable int venueId) {
    IdValidator.validateIdPositive(venueId);

    VenueDetailsProjection response = venueService.getVenueDetailsById(venueId);
    return ResponseEntity.ok(response);
  }
}
