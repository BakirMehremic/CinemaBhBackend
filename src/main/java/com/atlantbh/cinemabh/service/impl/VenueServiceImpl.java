package com.atlantbh.cinemabh.service.impl;

import com.atlantbh.cinemabh.dto.response.NameIdPair;
import com.atlantbh.cinemabh.dto.response.VenuePreviewResponse;
import com.atlantbh.cinemabh.entity.Venue;
import com.atlantbh.cinemabh.exception.NotFoundException;
import com.atlantbh.cinemabh.mapper.VenueMapper;
import com.atlantbh.cinemabh.projection.VenueBasicInfoProjection;
import com.atlantbh.cinemabh.projection.VenueDetailsProjection;
import com.atlantbh.cinemabh.repository.VenueRepository;
import com.atlantbh.cinemabh.service.VenueService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class VenueServiceImpl implements VenueService {
  private final VenueRepository venueRepository;
  private final VenueMapper venueMapper;

  @Override
  @Transactional(readOnly = true)
  public Page<VenuePreviewResponse> getVenuePreviewsPaginated(int pageNumber, int pageSize) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);

    Page<Long> pagedIds = venueRepository.getVenueIdsPaginated(pageable);
    if (pagedIds.isEmpty()) {
      return new PageImpl<>(List.of(), pageable, pagedIds.getTotalElements());
    }

    List<Venue> venues = venueRepository.getVenuesWithCitiesByIds(pagedIds.getContent());

    List<VenuePreviewResponse> content =
        venueMapper.toPreviewResponseList(pagedIds.getContent(), venues);

    return new PageImpl<>(content, pageable, pagedIds.getTotalElements());
  }

  @Override
  @Transactional(readOnly = true)
  public List<NameIdPair> getAllVenueNameIdPairs(Integer cityId) {
    return venueRepository.getAllVenueNameIdPairs(cityId);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<VenueBasicInfoProjection> getVenuesBasicInfoPaginated(
      int pageNumber, int pageSize, Integer cityId, String name) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);

    return venueRepository.getVenuesBasicInfoPaginated(pageable, cityId, name);
  }

  @Override
  @Transactional(readOnly = true)
  public VenueDetailsProjection getVenueDetailsById(Integer cityId) {
    return venueRepository
        .getVenueDetailsById(cityId)
        .orElseThrow(() -> new NotFoundException("Venue not found with city id: " + cityId));
  }
}
