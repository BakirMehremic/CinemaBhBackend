package com.atlantbh.cinemabh.service;

import com.atlantbh.cinemabh.dto.response.VenuePreviewResponse;
import org.springframework.data.domain.Page;

public interface VenueService {
  Page<VenuePreviewResponse> getVenuePreviewsPaginated(int pageNumber, int pageSize);
}
