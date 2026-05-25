package com.atlantbh.cinemabh.repository;

import com.atlantbh.cinemabh.entity.Movie;
import com.atlantbh.cinemabh.projection.MovieDetailsProjection;
import com.atlantbh.cinemabh.projection.MovieShowingProjection;
import com.atlantbh.cinemabh.projection.MovieUpcomingProjection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
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
              m.end_showing_date as endShowingDate, p.image_path as imageUrl,
              prj.startTimes, gen.genres
    FROM movies m
    LEFT JOIN photos p
    ON p.movie_id=m.id AND p.is_cover_photo=true
    JOIN (
        SELECT pr.movie_id, json_agg(pr.start_time ORDER BY pr.start_time) AS startTimes
        FROM projections pr
        JOIN halls h
        ON h.id = pr.hall_id
        JOIN venues v
        ON v.id = h.venue_id
        WHERE
                (CAST(:projectionDate AS date) IS NULL OR
                    pr.start_time >= CAST(:projectionDate as date) AND pr.start_time < CAST(:projectionDate AS date) + INTERVAL '1 day' )
                AND (CAST(:projectionTime AS time) IS NULL OR pr.start_time::time = CAST(:projectionTime AS time) )
                AND (:cityId IS NULL OR v.city_id = :cityId)
                AND (:venueId IS NULL OR v.id = :venueId)
        GROUP BY pr.movie_id
    ) prj
    ON prj.movie_id = m.id
    LEFT JOIN (
        SELECT mg.movie_id,
               array_agg(g.name ORDER BY g.name) as genres
        FROM movies_genres mg
        JOIN genres g ON g.id=mg.genre_id
        GROUP BY mg.movie_id
    ) gen
    ON gen.movie_id = m.id
    WHERE m.status='PUBLISHED'
    AND (CURRENT_TIMESTAMP AT TIME ZONE 'Europe/Sarajevo')::date BETWEEN m.start_showing_date AND m.end_showing_date
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
  Page<Movie> getMoviesShowingPreviewsByVenueId(Pageable pageable, @Param("venueId") long venueId);

  @Query(
      value =
"""
    SELECT m.id, m.name, m.duration_minutes as durationMinutes,
     p.image_path as coverPhotoUrl, gen.genres, m.start_showing_date as opensDate
    FROM movies m
    JOIN photos p
    ON p.movie_id=m.id AND p.is_cover_photo=true
    LEFT JOIN (
        SELECT mg.movie_id,
               array_agg(g.name ORDER BY g.name) as genres
        FROM movies_genres mg
        JOIN genres g ON g.id=mg.genre_id
        GROUP BY mg.movie_id
    ) gen
    ON gen.movie_id = m.id
    WHERE m.status='PUBLISHED'
    AND (CURRENT_TIMESTAMP AT TIME ZONE 'Europe/Sarajevo')::date < m.start_showing_date
      AND EXISTS (
          SELECT 1 FROM projections pr
          JOIN halls h ON h.id = pr.hall_id
          JOIN venues v ON v.id = h.venue_id
          WHERE pr.movie_id = m.id
          AND (:cityId IS NULL OR v.city_id = :cityId)
          AND (:venueId IS NULL OR v.id = :venueId)
      )
    AND (:name='' OR :name IS NULL OR LOWER(m.name) LIKE CONCAT('%', LOWER(:name), '%') ESCAPE '\\')
    AND (CAST(:startShowingDateFrom AS date) IS NULL OR m.start_showing_date >= CAST(:startShowingDateFrom AS date))
    AND (CAST(:startShowingDateTo AS date) IS NULL OR m.start_showing_date <= CAST(:startShowingDateTo AS date))
    AND (:genreId IS NULL OR m.id IN (
            SELECT mg2.movie_id FROM movies_genres mg2 WHERE mg2.genre_id = :genreId
        ))
""",
      nativeQuery = true)
  Page<MovieUpcomingProjection> filterUpcomingMoviesPaginated(
      Pageable pageable,
      @Param("startShowingDateFrom") LocalDate startShowingDateFrom,
      @Param("startShowingDateTo") LocalDate startShowingDateTo,
      @Param("name") String name,
      @Param("cityId") Long cityId,
      @Param("venueId") Long venueId,
      @Param("genreId") Long genreId);

  @Query(
      value =
          """
                      SELECT m.name AS name,
                             m.trailer_link AS trailerLink,
                             m.pg_rating AS pgRating,
                             m.language AS language,
                             m.duration_minutes AS durationMinutes,
                             m.start_showing_date AS startShowingDate,
                             m.end_showing_date AS endShowingDate,
                             m.synopsis AS synopsis,
                             m.rotten_tomatoes_rating AS rottenTomatoesRating,
                             m.imdb_rating AS imdbRating,
                             img.images AS images,
                             gen.genres AS genres,
                                pers.directors AS directors,
                                    pers.writers AS writers,
                                    pers.cast_members AS cast
                      FROM movies m
                      LEFT JOIN (
                          SELECT p.movie_id,
                                 array_agg(p.image_path) AS images
                          FROM photos p
                          GROUP BY p.movie_id
                      ) img ON img.movie_id = m.id
                      LEFT JOIN (
                          SELECT mg.movie_id,
                                 array_agg(g.name ORDER BY g.name) AS genres
                            FROM movies_genres mg
                            JOIN genres g ON g.id = mg.genre_id
                            GROUP BY mg.movie_id
                        ) gen ON gen.movie_id = m.id
                     LEFT JOIN (
                         SELECT mp.movie_id,
                                array_agg(p.name  ORDER BY p.name) FILTER (WHERE p.type = 'DIRECTOR') AS directors,
                                array_agg(p.name ORDER BY p.name) FILTER (WHERE p.type = 'WRITER') AS writers,
                                array_agg(p.name ORDER BY p.name) FILTER (WHERE p.type = 'CAST') AS cast_members
                         FROM movies_personnel mp
                         JOIN personnel p ON p.id = mp.personnel_id
                         GROUP BY mp.movie_id
                     ) pers ON pers.movie_id = m.id
                      WHERE m.id = :movieId
                  """,
      nativeQuery = true)
  Optional<MovieDetailsProjection> getMovieDetailsById(@Param("movieId") Long movieId);
}
