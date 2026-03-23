package com.atlantbh.cinemabh.repository;

import com.atlantbh.cinemabh.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
  @Query(
      "SELECT DISTINCT m FROM Movie m "
          + "LEFT JOIN FETCH m.genres "
          + "LEFT JOIN FETCH m.photos p "
          + "WHERE CURRENT_DATE BETWEEN m.startShowingDate AND m.endShowingDate")
  Page<Movie> getShowingMoviesPaginated(Pageable pageable);

  @Query(
      "SELECT DISTINCT m FROM Movie m "
          + "LEFT JOIN FETCH m.genres "
          + "LEFT JOIN FETCH m.photos p "
          + "WHERE m.startShowingDate > CURRENT_DATE")
  Page<Movie> getUpcomingMoviesPaginated(Pageable pageable);
}
