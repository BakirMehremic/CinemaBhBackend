package com.atlantbh.cinemabh.mapper.impl;

import com.atlantbh.cinemabh.dto.response.MoviePreviewResponse;
import com.atlantbh.cinemabh.entity.Genre;
import com.atlantbh.cinemabh.entity.Movie;
import com.atlantbh.cinemabh.entity.Photo;
import com.atlantbh.cinemabh.mapper.MovieMapper;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class MovieMapperImpl implements MovieMapper {

  @Override
  public MoviePreviewResponse toPreviewResponse(Movie movie) {

    String coverPhotoUrl =
        movie.getPhotos().stream()
            .filter(Photo::isCoverPhoto)
            .findFirst()
            .map(Photo::getImageUrl)
            .orElse(null);

    List<String> genres = movie.getGenres().stream().map(Genre::getName).toList();

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
}
