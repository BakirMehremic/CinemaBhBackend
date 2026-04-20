package com.atlantbh.cinemabh.repository;

import com.atlantbh.cinemabh.dto.response.NameIdPair;
import com.atlantbh.cinemabh.entity.Venue;
import com.atlantbh.cinemabh.projection.VenueBasicInfoProjection;
import com.atlantbh.cinemabh.projection.VenueDetailsProjection;
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
public interface VenueRepository extends JpaRepository<Venue, Long> {
  @Query("SELECT v.id from Venue v")
  Page<Long> getVenueIdsPaginated(Pageable pageable);

  @EntityGraph(attributePaths = {"city"})
  @Query("SELECT v FROM Venue v WHERE v.id IN :ids")
  List<Venue> getVenuesWithCitiesByIds(@Param("ids") List<Long> ids);

  @Query(
      "SELECT new com.atlantbh.cinemabh.dto.response.NameIdPair(v.name, v.id) FROM Venue v "
          + "WHERE (:cityId IS NULL OR v.city.id=:cityId)")
  List<NameIdPair> getAllVenueNameIdPairs(@Param("cityId") Long cityId);

  @Query(
"""
    SELECT v.id as id, v.name as name, v.imageUrl as imageUrl
    FROM Venue v
    WHERE (:cityId IS NULL OR v.city.id=:cityId)
     AND (:name='' OR :name IS NULL OR LOWER(v.name) LIKE CONCAT('%', LOWER(:name), '%') ESCAPE '\\')
""")
  Page<VenueBasicInfoProjection> getVenuesBasicInfoPaginated(
      Pageable pageable, @Param("cityId") Long cityId, @Param("name") String name);

  @Query(
"""
    SELECT v.id as id, v.name as name, v.street as street,
           v.streetNumber as streetNumber, v.phone as phone,
           v.imageUrl as imageUrl, c.name as cityName
    FROM Venue v
    JOIN v.city c
    WHERE v.id = :venueId
""")
  Optional<VenueDetailsProjection> getVenueDetailsById(@Param("venueId") long venueId);
}
