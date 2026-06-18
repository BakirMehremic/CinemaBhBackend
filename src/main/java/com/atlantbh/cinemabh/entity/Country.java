package com.atlantbh.cinemabh.entity;

import static com.atlantbh.cinemabh.constant.LocationConstants.COUNTRY_NAME_MAX_LENGTH;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "countries")
public class Country {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  private Long id;

  @Column(nullable = false, unique = true, length = COUNTRY_NAME_MAX_LENGTH)
  private String name;

  @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
  private Set<City> cities = new HashSet<>();
}
