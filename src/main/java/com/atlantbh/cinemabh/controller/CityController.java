package com.atlantbh.cinemabh.controller;

import com.atlantbh.cinemabh.dto.response.NameIdPair;
import com.atlantbh.cinemabh.service.CityService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/cities")
public class CityController {
  private final CityService cityService;

  @GetMapping("/names")
  public ResponseEntity<List<NameIdPair>> getCityNameIdPairs() {
    List<NameIdPair> response = cityService.getAllCityNameIdPairs();
    return ResponseEntity.ok(response);
  }
}
