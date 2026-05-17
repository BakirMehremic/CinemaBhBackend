package com.atlantbh.cinemabh.entity;

import static com.atlantbh.cinemabh.constant.AuthConstants.REFRESH_TOKEN_HASH_LENGTH;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Getter
@Setter
@Table(name = "refresh_tokens")
public class RefreshToken {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @CreationTimestamp
  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime expiresAt;

  @Column(nullable = false, unique = true, length = REFRESH_TOKEN_HASH_LENGTH)
  private String tokenHash;
}
