package com.atlantbh.cinemabh.mapper;

import com.atlantbh.cinemabh.dto.response.VenuePreviewResponse;
import com.atlantbh.cinemabh.entity.Venue;
import java.util.List;

public interface VenueMapper {
  VenuePreviewResponse toPreviewResponse(Venue venue);

  List<VenuePreviewResponse> toPreviewResponseList(List<Long> ids, List<Venue> venues);
}
