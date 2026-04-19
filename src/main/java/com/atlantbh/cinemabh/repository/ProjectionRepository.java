package com.atlantbh.cinemabh.repository;

import com.atlantbh.cinemabh.entity.Projection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectionRepository extends JpaRepository<Projection, Long> {
  @Query(
      nativeQuery = true,
      value =
"""
    SELECT DISTINCT CAST(p.start_time AS time) as times
    FROM projections p
    JOIN halls h ON h.id=p.hall_id
    JOIN venues v ON v.id=h.venue_id
    JOIN cities c ON c.id=v.city_id
    JOIN movies m ON m.id=p.movie_id
    JOIN movies_genres mg ON mg.movie_id=m.id
    WHERE m.status='PUBLISHED'
    AND CURRENT_DATE BETWEEN m.start_showing_date AND m.end_showing_date
    AND (:movieName='' OR :movieName IS NULL OR LOWER(m.name) LIKE CONCAT('%', LOWER(:movieName), '%') ESCAPE '\\')
    AND (:cityId IS NULL OR c.id = :cityId)
    AND (:venueId IS NULL OR v.id = :venueId)
    AND (:genreId IS NULL OR mg.genre_id = :genreId)
    AND (CAST(:date AS date) IS NULL OR DATE(p.start_time) = CAST(:date AS date))
    ORDER BY times
""")
  List<LocalTime> getShowingMoviesProjectionTimes(
      @Param("movieName") String movieName,
      @Param("cityId") Integer cityId,
      @Param("venueId") Integer venueId,
      @Param("genreId") Integer genreId,
      @Param("date") LocalDate date);
}
