package com.atlantbh.cinemabh.repository;

import com.atlantbh.cinemabh.entity.Movie;
import com.atlantbh.cinemabh.projection.MovieShowingProjection;
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
      value =
"""
    SELECT m.id, m.name, m.pg_rating as pgRating, m.language, m.duration_minutes as durationMinutes,
              m.end_showing_date as endShowingDate, p.image_url as imageUrl,
              prj.startTimes, gen.genres
    FROM movies m
    JOIN photos p
    ON p.movie_id=m.id AND p.is_cover_photo=true
    JOIN (
        SELECT pr.movie_id, json_agg(pr.start_time ORDER BY pr.start_time) AS startTimes
        FROM projections pr
        JOIN halls h
        ON h.id = pr.hall_id
        JOIN venues v
        ON v.id = h.venue_id
        WHERE
              (CAST(:projectionDate AS date) IS NULL OR DATE(pr.start_time) = CAST(:projectionDate AS date) )
                AND (CAST(:projectionTime AS time) IS NULL OR pr.start_time::time = CAST(:projectionTime AS time) )
                AND (:cityId IS NULL OR v.city_id = :cityId)
                AND (:venueId IS NULL OR v.id = :venueId)
        GROUP BY pr.movie_id
    ) prj
    ON prj.movie_id = m.id
    LEFT JOIN (
        SELECT mg.movie_id,
               json_agg(g.name ORDER BY g.name) as genres
        FROM movies_genres mg
        JOIN genres g ON g.id=mg.genre_id
        GROUP BY mg.movie_id
    ) gen
    ON gen.movie_id = m.id
    WHERE m.status='PUBLISHED'
    AND CURRENT_DATE BETWEEN m.start_showing_date AND m.end_showing_date
    AND (:name='' OR :name IS NULL OR LOWER(m.name) LIKE CONCAT('%', LOWER(:name), '%') ESCAPE '\\')
    AND (:genreId IS NULL OR m.id IN (
        SELECT mg2.movie_id FROM movies_genres mg2 WHERE mg2.genre_id = :genreId
    ))
""",
      nativeQuery = true)
  Page<MovieShowingProjection> filterShowingMoviesPaginated(
      Pageable pageable,
      @Param("projectionDate") LocalDate projectionDate,
      @Param("projectionTime") LocalTime projectionTime,
      @Param("name") String name,
      @Param("cityId") Long cityId,
      @Param("venueId") Long venueId,
      @Param("genreId") Long genreId);

  // TODO rewrite dont forget to add status = published
  @Query(
"""
  SELECT DISTINCT m
  FROM Movie m
  JOIN m.projections p
  JOIN p.hall h
  JOIN h.venue v
  WHERE v.id = :venueId
  AND CURRENT_DATE BETWEEN m.startShowingDate AND m.endShowingDate
  AND m.moviePublishedStatus=PUBLISHED
""")
  Page<Movie> getMoviesShowingPreviewsByVenueId(
      Pageable pageable, @Param("venueId") Integer venueId);
}
