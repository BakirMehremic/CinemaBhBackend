package com.atlantbh.cinemabh.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "projections")
public class Projection {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private LocalDateTime startTime;

  @Column(nullable = false)
  private LocalDateTime endTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "movie_id", nullable = false)
  private Movie movie;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "hall_id", nullable = false)
  private Hall hall;
}
