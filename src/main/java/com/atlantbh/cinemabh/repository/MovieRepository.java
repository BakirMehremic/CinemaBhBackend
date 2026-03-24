package com.atlantbh.cinemabh.repository;

import com.atlantbh.cinemabh.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

  @EntityGraph(attributePaths = {"genres", "photos"})
  @Query("SELECT m FROM Movie m WHERE CURRENT_DATE BETWEEN m.startShowingDate AND m.endShowingDate")
  Page<Movie> getShowingMoviesPaginated(Pageable pageable);

  @EntityGraph(attributePaths = {"genres", "photos"})
  @Query("SELECT m FROM Movie m WHERE m.startShowingDate > CURRENT_DATE")
  Page<Movie> getUpcomingMoviesPaginated(Pageable pageable);
}
