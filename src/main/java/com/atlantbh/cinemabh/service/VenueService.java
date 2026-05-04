package com.atlantbh.cinemabh.service;

import com.atlantbh.cinemabh.dto.request.common.PaginationRequest;
import com.atlantbh.cinemabh.dto.request.venue.FilterVenuesBasicInfo;
import com.atlantbh.cinemabh.dto.response.NameIdPair;
import com.atlantbh.cinemabh.dto.response.VenuePreviewResponse;
import com.atlantbh.cinemabh.projection.VenueBasicInfoProjection;
import com.atlantbh.cinemabh.projection.VenueDetailsProjection;
import java.util.List;
import org.springframework.data.domain.Page;

public interface VenueService {
  Page<VenuePreviewResponse> getVenuePreviewsPaginated(PaginationRequest paginationRequest);

  List<NameIdPair> getAllVenueNameIdPairs(Long cityId);

  Page<VenueBasicInfoProjection> getVenuesBasicInfoPaginated(FilterVenuesBasicInfo filter);

  VenueDetailsProjection getVenueDetailsById(long venueId);
}
