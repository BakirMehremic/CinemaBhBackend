package com.atlantbh.cinemabh.mapper.impl;

import com.atlantbh.cinemabh.dto.response.VenuePreviewResponse;
import com.atlantbh.cinemabh.entity.Venue;
import com.atlantbh.cinemabh.mapper.VenueMapper;
import org.springframework.stereotype.Component;

@Component
public class VenueMapperImpl implements VenueMapper {

  @Override
  public VenuePreviewResponse toPreviewResponse(Venue venue) {
    return new VenuePreviewResponse(
        venue.getStreet(), venue.getStreetNumber(), venue.getCity().getName());
  }
}
