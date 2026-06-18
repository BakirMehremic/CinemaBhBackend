package com.atlantbh.cinemabh.entity;

import static com.atlantbh.cinemabh.constant.GenreConstants.GENRE_NAME_MAX_LENGTH;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "genres")
public class Genre {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  private Long id;

  @Column(nullable = false, length = GENRE_NAME_MAX_LENGTH)
  private String name;

  @ManyToMany(mappedBy = "genres")
  private Set<Movie> movies = new HashSet<>();
}
