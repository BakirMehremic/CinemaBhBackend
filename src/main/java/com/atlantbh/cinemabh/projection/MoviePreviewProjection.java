package com.atlantbh.cinemabh.projection;

import java.util.List;

public interface MoviePreviewProjection {
  String getName();

  long getId();

  int getDurationMinutes();

  String getCoverPhotoUrl();

  String getSynopsis();

  List<String> getGenres();
}
