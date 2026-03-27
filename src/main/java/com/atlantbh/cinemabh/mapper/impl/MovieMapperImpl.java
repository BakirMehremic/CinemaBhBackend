package com.atlantbh.cinemabh.mapper.impl;

import com.atlantbh.cinemabh.dto.response.MoviePreviewResponse;
import com.atlantbh.cinemabh.entity.Genre;
import com.atlantbh.cinemabh.entity.Movie;
import com.atlantbh.cinemabh.entity.Photo;
import com.atlantbh.cinemabh.mapper.MovieMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        movie.getName(), movie.getId(), movie.getDurationInMinutes(), coverPhotoUrl, genres);
  }

  @Override
  public List<MoviePreviewResponse> toPreviewResponseList(List<Long> ids, List<Movie> movies) {
    Map<Long, Movie> moviesMap = new HashMap<>();
    movies.forEach(movie -> moviesMap.put(movie.getId(), movie));

    List<MoviePreviewResponse> content = new ArrayList<>();
    for (long id : ids) {
      content.add(this.toPreviewResponse(moviesMap.get(id)));
    }

    return content;
  }
}
