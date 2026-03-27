package com.atlantbh.cinemabh.mapper.impl;

import com.atlantbh.cinemabh.dto.response.VenuePreviewResponse;
import com.atlantbh.cinemabh.entity.Venue;
import com.atlantbh.cinemabh.mapper.VenueMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class VenueMapperImpl implements VenueMapper {

  @Override
  public VenuePreviewResponse toPreviewResponse(Venue venue) {
    return new VenuePreviewResponse(
        venue.getStreet(), venue.getStreetNumber(), venue.getCity().getName());
  }

  @Override
  public List<VenuePreviewResponse> toPreviewResponseList(List<Long> ids, List<Venue> venues) {
    Map<Long, Venue> venueMap = new HashMap<>();
    venues.forEach(venue -> venueMap.put(venue.getId(), venue));

    List<VenuePreviewResponse> content = new ArrayList<>();
    for (long id : ids) {
      content.add(this.toPreviewResponse(venueMap.get(id)));
    }

    return content;
  }
}
