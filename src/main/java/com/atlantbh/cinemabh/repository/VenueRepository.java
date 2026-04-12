package com.atlantbh.cinemabh.repository;

import com.atlantbh.cinemabh.dto.response.NameIdPair;
import com.atlantbh.cinemabh.entity.Venue;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
  @Query("SELECT v.id from Venue v")
  Page<Long> getVenueIdsPaginated(Pageable pageable);

  @EntityGraph(attributePaths = {"city"})
  @Query("SELECT v FROM Venue v WHERE v.id IN :ids")
  List<Venue> getVenuesWithCitiesByIds(@Param("ids") List<Long> ids);

  @Query(
      "SELECT new com.atlantbh.cinemabh.dto.response.NameIdPair(v.name, v.id) FROM Venue v "
          + "WHERE (:cityId IS NULL OR v.city.id=:cityId)")
  List<NameIdPair> getAllVenueNameIdPairs(@Param("cityId") Integer cityId);
}
