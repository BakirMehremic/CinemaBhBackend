package com.atlantbh.cinemabh.projection;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface MovieDetailsProjection {
  Long getId();

  String getName();

  String getTrailerLink();

  List<String> getImages();

  String getPgRating();

  String getLanguage();

  int getDurationMinutes();

  LocalDate getStartShowingDate();

  LocalDate getEndShowingDate();

  List<String> getGenres();

  String getSynopsis();

  List<String> getDirectors();

  List<String> getWriters();

  List<String> getActors();

  Short getRottenTomatoesRating();

  BigDecimal getImdbRating();
}
