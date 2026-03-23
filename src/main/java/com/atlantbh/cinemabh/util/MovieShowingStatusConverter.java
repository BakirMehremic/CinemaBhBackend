package com.atlantbh.cinemabh.util;

import com.atlantbh.cinemabh.enums.MovieShowingStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class MovieShowingStatusConverter implements Converter<String, MovieShowingStatus> {

  @Override
  public MovieShowingStatus convert(String source) {
    return MovieShowingStatus.valueOf(source.toUpperCase());
  }
}
