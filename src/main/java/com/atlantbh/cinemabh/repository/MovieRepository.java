package com.atlantbh.cinemabh.repository;

import com.atlantbh.cinemabh.entity.Movie;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
  @Query(
      "SELECT m.id FROM Movie m WHERE m.startShowingDate > CURRENT_DATE "
          + "AND m.moviePublishedStatus=PUBLISHED")
  Page<Long> getUpcomingMovieIds(Pageable pageable);

  @Query(
      "SELECT m.id FROM Movie m WHERE CURRENT_DATE BETWEEN m.startShowingDate AND m.endShowingDate "
          + "AND m.moviePublishedStatus=PUBLISHED")
  Page<Long> getShowingMovieIds(Pageable pageable);

  @EntityGraph(attributePaths = {"genres", "photos"})
  @Query("SELECT m FROM Movie m WHERE m.id in :ids")
  List<Movie> getMoviesWithGenresAndPhotosByIds(@Param("ids") List<Long> ids);
}
