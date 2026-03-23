package com.atlantbh.cinemabh.repository;

import com.atlantbh.cinemabh.entity.Venue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface VenueRepository extends JpaRepository<Venue, Long> {
  @Query("SELECT DISTINCT v FROM Venue v LEFT JOIN FETCH v.city")
  Page<Venue> getVenuesPaginated(Pageable pageable);
}
