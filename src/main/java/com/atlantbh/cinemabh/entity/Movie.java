package com.atlantbh.cinemabh.entity;

import com.atlantbh.cinemabh.enums.DraftStepStatus;
import com.atlantbh.cinemabh.enums.MoviePublishedStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "movies")
public class Movie {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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
  private BigDecimal imdbRating;

  @Column(precision = 5, scale = 1)
  private BigDecimal rottenTomatoesRating;

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
}
