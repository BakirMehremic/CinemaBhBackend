package com.atlantbh.cinemabh.mapper;

import com.atlantbh.cinemabh.dto.response.VenuePreviewResponse;
import com.atlantbh.cinemabh.entity.Venue;

public interface VenueMapper {
  VenuePreviewResponse toPreviewResponse(Venue venue);
}
