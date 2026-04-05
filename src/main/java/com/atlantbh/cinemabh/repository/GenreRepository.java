package com.atlantbh.cinemabh.repository;

import com.atlantbh.cinemabh.dto.response.NameIdPair;
import com.atlantbh.cinemabh.entity.Genre;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
  @Query(
      "SELECT new com.atlantbh.cinemabh.dto.response.NameIdPair(g.name, g.id) FROM Genre g "
          + "JOIN g.movies m "
          + "WHERE CURRENT_DATE BETWEEN m.startShowingDate AND m.endShowingDate ")
  List<NameIdPair> getAllGenreNameIdPairs();
}
