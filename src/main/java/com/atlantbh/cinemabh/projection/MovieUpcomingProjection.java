package com.atlantbh.cinemabh.projection;

import java.time.LocalDate;
import java.util.List;

public interface MovieUpcomingProjection {
  Long getId();

  String getName();

  int getDurationMinutes();

  LocalDate getOpensDate();

  String getCoverPhotoUrl();

  List<String> getGenres();
}
