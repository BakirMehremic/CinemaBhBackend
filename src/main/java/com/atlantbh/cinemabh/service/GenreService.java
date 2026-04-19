package com.atlantbh.cinemabh.service;

import com.atlantbh.cinemabh.dto.response.NameIdPair;

import java.util.List;

public interface GenreService {
  List<NameIdPair> getAllGenreNameIdPairs();
}
