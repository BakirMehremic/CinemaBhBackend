package com.atlantbh.cinemabh.controller;

import com.atlantbh.cinemabh.dto.response.NameIdPair;
import com.atlantbh.cinemabh.dto.response.PaginatedResponse;
import com.atlantbh.cinemabh.dto.response.VenuePreviewResponse;
import com.atlantbh.cinemabh.service.VenueService;
import com.atlantbh.cinemabh.validator.PaginationValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/venues")
public class VenueController {
  private final VenueService venueService;
  private final PaginationValidator paginationValidator;

  @GetMapping("/preview")
  public ResponseEntity<PaginatedResponse<VenuePreviewResponse>> getVenuesPreview(
      @RequestParam(defaultValue = "0") int pageNumber,
      @RequestParam(defaultValue = "4") int pageSize) {
    paginationValidator.validate(pageNumber, pageSize);

    Page<VenuePreviewResponse> venues =
        venueService.getVenuePreviewsPaginated(pageNumber, pageSize);

    PaginatedResponse<VenuePreviewResponse> response = PaginatedResponse.from(venues);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/names")
  public ResponseEntity<List<NameIdPair>> getVenuesNameIdPairs() {
    List<NameIdPair> response = venueService.getAllVenueNameIdPairs();
    return ResponseEntity.ok(response);
  }
}
