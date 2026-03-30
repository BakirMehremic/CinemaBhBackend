package com.atlantbh.cinemabh.mapper.impl;

import com.atlantbh.cinemabh.dto.response.VenuePreviewResponse;
import com.atlantbh.cinemabh.entity.Venue;
import com.atlantbh.cinemabh.mapper.VenueMapper;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class VenueMapperImpl implements VenueMapper {

  @Override
  public VenuePreviewResponse toPreviewResponse(Venue venue) {
    return new VenuePreviewResponse(
        venue.getName(),
        venue.getStreet(),
        venue.getStreetNumber(),
        venue.getCity().getName(),
        venue.getImageUrl());
  }

  @Override
  public List<VenuePreviewResponse> toPreviewResponseList(List<Long> ids, List<Venue> venues) {
    Map<Long, Venue> venueMap =
        venues.stream().collect(Collectors.toMap(Venue::getId, venue -> venue));

    return ids.stream().map(id -> this.toPreviewResponse(venueMap.get(id))).toList();
  }
}
