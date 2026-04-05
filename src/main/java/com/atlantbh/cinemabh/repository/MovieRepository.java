package com.atlantbh.cinemabh.repository;

import com.atlantbh.cinemabh.entity.Movie;
import java.time.LocalDate;
import java.time.LocalTime;
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
  Page<Long> getUpcomingMovieIdsPaginated(Pageable pageable);

  @Query(
      "SELECT m.id FROM Movie m WHERE CURRENT_DATE BETWEEN m.startShowingDate AND m.endShowingDate "
          + "AND m.moviePublishedStatus=PUBLISHED")
  Page<Long> getShowingMovieIdsPaginated(Pageable pageable);

  @EntityGraph(attributePaths = {"genres", "photos"})
  @Query("SELECT m FROM Movie m WHERE m.id in :ids")
  List<Movie> getMoviesWithGenresAndPhotosByIds(@Param("ids") List<Long> ids);

  @Query(
      "SELECT m.id FROM Movie m "
          + "JOIN m.projections p "
          + "JOIN p.hall h "
          + "JOIN h.venue v "
          + "JOIN v.city c "
          + "LEFT JOIN m.genres g "
          + "WHERE CAST(:projectionDate AS date) IS NULL OR CAST(p.startTime as date)=:projectionDate "
          + "AND CAST(:projectionTime as time) IS NULL OR CAST(p.startTime as time)=:projectionTime "
          + "AND (:name='' OR :name IS NULL OR LOWER(m.name) LIKE ('%' || LOWER(:name) || '%'))"
          + "AND (:cityId IS NULL OR c.id=:cityId) "
          + "AND (:venueId IS NULL OR v.id=:venueId) "
          + "AND (:genreId IS NULL OR g.id=:genreId) "
          + "and CURRENT_DATE BETWEEN m.startShowingDate AND m.endShowingDate "
          + "AND m.moviePublishedStatus=PUBLISHED ")
  Page<Long> getShowingMovieIdsFilteredPaginated(
      Pageable pageable,
      @Param("projectionDate") LocalDate projectionDate,
      @Param("projectionTime") LocalTime projectionTime,
      @Param("name") String name,
      @Param("cityId") Long cityId,
      @Param("venueId") Long venueId,
      @Param("genreId") Long genreId);

  @EntityGraph(attributePaths = {"genres", "projections", "photos"})
  @Query("SELECT m FROM Movie m WHERE m.id in :ids")
  List<Movie> getMoviesWithGenresProjectionsPhotosByIds(@Param("ids") List<Long> ids);
}
