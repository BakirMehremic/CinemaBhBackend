package com.atlantbh.cinemabh.service.impl;

import com.atlantbh.cinemabh.dto.response.MoviePreviewResponse;
import com.atlantbh.cinemabh.entity.Movie;
import com.atlantbh.cinemabh.enums.MovieShowingStatus;
import com.atlantbh.cinemabh.mapper.MovieMapper;
import com.atlantbh.cinemabh.repository.MovieRepository;
import com.atlantbh.cinemabh.service.MovieService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MovieServiceImpl implements MovieService {
  private final MovieRepository movieRepository;
  private final MovieMapper movieMapper;

  public MovieServiceImpl(MovieRepository movieRepository, MovieMapper movieMapper) {
    this.movieRepository = movieRepository;
    this.movieMapper = movieMapper;
  }

  @Transactional(readOnly = true)
  public Page<MoviePreviewResponse> getMoviesPreviewPaginated(
      Integer pageNumber, Integer pageSize, MovieShowingStatus showingStatus) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);

    Page<Movie> moviesPage =
        switch (showingStatus) {
          case UPCOMING -> movieRepository.getUpcomingMoviesPaginated(pageable);
          case SHOWING -> movieRepository.getShowingMoviesPaginated(pageable);
        };

    return moviesPage.map(movieMapper::toPreviewResponse);
  }
}
