package com.atlantbh.cinemabh.mapper.impl;

import com.atlantbh.cinemabh.dto.response.MoviePreviewResponse;
import com.atlantbh.cinemabh.entity.Genre;
import com.atlantbh.cinemabh.entity.Movie;
import com.atlantbh.cinemabh.entity.Photo;
import com.atlantbh.cinemabh.mapper.MovieMapper;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MovieMapperImpl implements MovieMapper {

  public MoviePreviewResponse toPreviewResponse(Movie movie) {

    String coverPhotoUrl =
        movie.getPhotos().stream()
            .filter(photo -> Boolean.TRUE.equals(photo.getIsCoverPhoto()))
            .findFirst()
            .map(Photo::getImageUrl)
            .orElse(null);

    List<String> genres = movie.getGenres().stream().map(Genre::getName).toList();

    return new MoviePreviewResponse(
        movie.getName(), movie.getId(), movie.getDuration(), coverPhotoUrl, genres);
  }
}
