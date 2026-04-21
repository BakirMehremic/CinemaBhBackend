package com.atlantbh.cinemabh.service.impl;

import com.atlantbh.cinemabh.dto.response.NameIdPair;
import com.atlantbh.cinemabh.repository.CityRepository;
import com.atlantbh.cinemabh.service.CityService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CityServiceImpl implements CityService {
  private final CityRepository cityRepository;

  @Override
  @Transactional(readOnly = true)
  public List<NameIdPair> getAllCityNameIdPairs() {
    return cityRepository.getAllCityNameIdPairs();
  }
}
