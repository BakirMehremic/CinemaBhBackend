package com.atlantbh.cinemabh.projection;

import java.time.LocalDate;
import java.util.List;

public interface MovieUpcomingProjection {
  long getId();

  String getName();

  int getDurationMinutes();

  LocalDate getOpensDate();

  String getImageUrl();

  List<String> getGenres();
}
