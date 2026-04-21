package com.atlantbh.cinemabh.repository;

import com.atlantbh.cinemabh.dto.response.NameIdPair;
import com.atlantbh.cinemabh.entity.City;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
  @Query("SELECT new com.atlantbh.cinemabh.dto.response.NameIdPair(c.name, c.id) FROM City c")
  List<NameIdPair> getAllCityNameIdPairs();
}
