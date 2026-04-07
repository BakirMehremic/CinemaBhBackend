package com.atlantbh.cinemabh.mapper.impl;

import com.atlantbh.cinemabh.dto.response.MoviePreviewResponse;
import com.atlantbh.cinemabh.dto.response.MovieShowingResponse;
import com.atlantbh.cinemabh.entity.Genre;
import com.atlantbh.cinemabh.entity.Movie;
import com.atlantbh.cinemabh.entity.Photo;
import com.atlantbh.cinemabh.entity.Projection;
import com.atlantbh.cinemabh.mapper.MovieMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class MovieMapperImpl implements MovieMapper {

  @Override
  public MoviePreviewResponse toPreviewResponse(Movie movie) {

    String coverPhotoUrl = getCoverPhotoUrl(movie);

    List<String> genres = getGenreNames(movie);

    return new MoviePreviewResponse(
        movie.getName(),
        movie.getId(),
        movie.getDurationInMinutes(),
        coverPhotoUrl,
        movie.getSynopsis(),
        genres);
  }

  @Override
  public List<MoviePreviewResponse> toPreviewResponseList(List<Long> ids, List<Movie> movies) {
    Map<Long, Movie> movieMap =
        movies.stream().collect(Collectors.toMap(Movie::getId, movie -> movie));

    return ids.stream().map(id -> this.toPreviewResponse(movieMap.get(id))).toList();
  }

  @Override
  public MovieShowingResponse toShowingResponse(Movie movie) {
    String coverPhotoUrl = getCoverPhotoUrl(movie);

    List<String> genres = getGenreNames(movie);

    List<LocalTime> projectionTimes = new ArrayList<>();

    LocalDate lastProjectionDate =
        movie.getProjections().stream()
            .map(Projection::getStartTime)
            .peek(dt -> projectionTimes.add(dt.toLocalTime()))
            .max(LocalDateTime::compareTo)
            .map(LocalDateTime::toLocalDate)
            .orElse(null);

    return new MovieShowingResponse(
        movie.getId(),
        movie.getName(),
        coverPhotoUrl,
        movie.getPgRating(),
        movie.getLanguage(),
        movie.getDurationInMinutes(),
        genres,
        projectionTimes,
        lastProjectionDate);
  }

  @Override
  public List<MovieShowingResponse> toShowingResponseList(List<Long> ids, List<Movie> movies) {
    Map<Long, Movie> movieMap =
        movies.stream().collect(Collectors.toMap(Movie::getId, movie -> movie));

    return ids.stream().map(id -> this.toShowingResponse(movieMap.get(id))).toList();
  }

  private String getCoverPhotoUrl(Movie movie) {
    return movie.getPhotos().stream()
        .filter(Photo::isCoverPhoto)
        .findFirst()
        .map(Photo::getImageUrl)
        .orElse(null);
  }

  private List<String> getGenreNames(Movie movie) {
    return movie.getGenres().stream().map(Genre::getName).toList();
  }
}
