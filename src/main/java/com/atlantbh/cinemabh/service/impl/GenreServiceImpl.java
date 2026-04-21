package com.atlantbh.cinemabh.service.impl;

import com.atlantbh.cinemabh.dto.response.NameIdPair;
import com.atlantbh.cinemabh.repository.GenreRepository;
import com.atlantbh.cinemabh.service.GenreService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
  private final GenreRepository genreRepository;

  @Override
  @Transactional(readOnly = true)
  public List<NameIdPair> getAllGenreNameIdPairs() {
    return genreRepository.getAllGenreNameIdPairs();
  }
}
