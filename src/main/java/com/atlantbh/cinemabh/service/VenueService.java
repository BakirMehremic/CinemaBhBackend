package com.atlantbh.cinemabh.service;

import com.atlantbh.cinemabh.dto.response.NameIdPair;
import com.atlantbh.cinemabh.dto.response.VenuePreviewResponse;
import com.atlantbh.cinemabh.projection.VenueBasicInfoProjection;
import com.atlantbh.cinemabh.projection.VenueDetailsProjection;
import java.util.List;
import org.springframework.data.domain.Page;

public interface VenueService {
  Page<VenuePreviewResponse> getVenuePreviewsPaginated(int pageNumber, int pageSize);

  List<NameIdPair> getAllVenueNameIdPairs(Long cityId);

  Page<VenueBasicInfoProjection> getVenuesBasicInfoPaginated(
      int pageNumber, int pageSize, Long cityId, String name);

  VenueDetailsProjection getVenueDetailsById(long venueId);
}
