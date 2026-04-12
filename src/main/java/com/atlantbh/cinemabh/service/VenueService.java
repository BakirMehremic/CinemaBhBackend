package com.atlantbh.cinemabh.service;

import com.atlantbh.cinemabh.dto.response.NameIdPair;
import com.atlantbh.cinemabh.dto.response.VenuePreviewResponse;
import java.util.List;
import org.springframework.data.domain.Page;

public interface VenueService {
  Page<VenuePreviewResponse> getVenuePreviewsPaginated(int pageNumber, int pageSize);

  List<NameIdPair> getAllVenueNameIdPairs(Integer cityId);
}
