package com.atlantbh.cinemabh.service.impl;

import com.atlantbh.cinemabh.dto.response.VenuePreviewResponse;
import com.atlantbh.cinemabh.entity.Venue;
import com.atlantbh.cinemabh.mapper.VenueMapper;
import com.atlantbh.cinemabh.repository.VenueRepository;
import com.atlantbh.cinemabh.service.VenueService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class VenueServiceImpl implements VenueService {
  private final VenueRepository venueRepository;
  private final VenueMapper venueMapper;

  public VenueServiceImpl(VenueRepository venueRepository, VenueMapper venueMapper) {
    this.venueRepository = venueRepository;
    this.venueMapper = venueMapper;
  }

  @Override
  public Page<VenuePreviewResponse> getVenuePreviewsPaginated(
      Integer pageNumber, Integer pageSize) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);

    Page<Venue> pagedResult = venueRepository.getVenuesPaginated(pageable);
    return pagedResult.map(venueMapper::toPreviewResponse);
  }
}
