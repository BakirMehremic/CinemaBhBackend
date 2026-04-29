package com.atlantbh.cinemabh.projection;

import java.time.LocalDate;
import java.util.List;

public interface MovieShowingProjection {
  Long getId();

  String getName();

  String getPgRating();

  String getLanguage();

  int getDurationMinutes();

  LocalDate getEndShowingDate();

  String getImageUrl();

  List<String> getStartTimes();

  List<String> getGenres();
}
