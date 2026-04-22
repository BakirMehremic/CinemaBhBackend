package com.atlantbh.cinemabh.projection;

import java.time.LocalDate;
import java.util.List;

public interface MovieUpcomingProjection {
  long getId();

  String getName();

  int getDuration();

  LocalDate getOpensDate();

  String getCoverPhotoUrl();

  List<String> getGenres();
}
