package com.atlantbh.cinemabh.entity;

import com.atlantbh.cinemabh.enums.DraftStepStatus;
import com.atlantbh.cinemabh.enums.MoviePublishedStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "movies")
public class Movie {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  private Long id;

  @Column(nullable = false)
  private String pgRating;

  @Column(nullable = false)
  private String language;

  @Column(nullable = false)
  private String name;

  @Column(name = "duration_minutes", nullable = false)
  private int durationInMinutes;

  @Column private String trailerLink;

  @Column private String synopsis;

  @Column(precision = 3, scale = 1)
  @DecimalMin(value = "1.0")
  @DecimalMax(value = "10.0")
  private BigDecimal imdbRating;

  @Column
  @Min(0)
  @Max(100)
  private Short rottenTomatoesRating;

  @Column(nullable = false)
  private LocalDate startShowingDate;

  @Column(nullable = false)
  private LocalDate endShowingDate;

  @Column(name = "draft_step")
  @Enumerated(EnumType.STRING)
  private DraftStepStatus draftStepStatus;

  @Column(name = "status", nullable = false)
  @Enumerated(EnumType.STRING)
  private MoviePublishedStatus moviePublishedStatus;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "movies_genres",
      joinColumns = @JoinColumn(name = "movie_id"),
      inverseJoinColumns = @JoinColumn(name = "genre_id"))
  private Set<Genre> genres = new HashSet<>();

  @OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
  private Set<Photo> photos = new HashSet<>();

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "movies_personnel",
      joinColumns = @JoinColumn(name = "movie_id"),
      inverseJoinColumns = @JoinColumn(name = "personnel_id"))
  @OrderColumn(name = "position", nullable = false)
  private List<Personnel> personnel = new ArrayList<>();
}
