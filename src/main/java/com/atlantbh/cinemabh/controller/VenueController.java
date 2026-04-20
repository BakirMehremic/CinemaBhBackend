package com.atlantbh.cinemabh.controller;

import com.atlantbh.cinemabh.dto.response.*;
import com.atlantbh.cinemabh.projection.VenueBasicInfoProjection;
import com.atlantbh.cinemabh.projection.VenueDetailsProjection;
import com.atlantbh.cinemabh.service.VenueService;
import com.atlantbh.cinemabh.validator.IdValidator;
import com.atlantbh.cinemabh.validator.PaginationValidator;
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
      @RequestParam(defaultValue = "0") int pageNumber,
      @RequestParam(defaultValue = "4") int pageSize) {
    PaginationValidator.validate(pageNumber, pageSize);

    return ResponseEntity.ok(
        PaginatedResponse.from(venueService.getVenuePreviewsPaginated(pageNumber, pageSize)));
  }

  @GetMapping("/names")
  public ResponseEntity<List<NameIdPair>> getVenuesNameIdPairs(
      @RequestParam(required = false) Long cityId) {
    IdValidator.validateIdNullOrPositive(cityId);

    return ResponseEntity.ok(venueService.getAllVenueNameIdPairs(cityId));
  }

  @GetMapping("/basic")
  public ResponseEntity<PaginatedResponse<VenueBasicInfoProjection>> getVenuesBasicInfo(
      @RequestParam(defaultValue = "0") int pageNumber,
      @RequestParam(defaultValue = "4") int pageSize,
      @RequestParam(required = false) Long cityId,
      @RequestParam(required = false) String name) {
    PaginationValidator.validate(pageNumber, pageSize);
    IdValidator.validateIdNullOrPositive(cityId);

    return ResponseEntity.ok(
        PaginatedResponse.from(
            venueService.getVenuesBasicInfoPaginated(pageNumber, pageSize, cityId, name)));
  }

  @GetMapping("/details/{venueId}")
  public ResponseEntity<VenueDetailsProjection> getVenueDetailsById(@PathVariable long venueId) {
    IdValidator.validateIdPositive(venueId);

    return ResponseEntity.ok(venueService.getVenueDetailsById(venueId));
  }
}
