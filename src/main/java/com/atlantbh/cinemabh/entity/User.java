package com.atlantbh.cinemabh.entity;

import com.atlantbh.cinemabh.enums.UserRole;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "role", columnDefinition = "user_role")
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  private UserRole userRole;

  @Column(nullable = false)
  private String passwordHash;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  @Column(nullable = false)
  private String street;

  @Column private String phoneNumber;

  @Column(unique = true, nullable = false)
  private String email;

  @Column private String imageUrl;

  @Column private LocalDateTime createdAt;

  @Column private Boolean isVerified;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "city_id", nullable = false)
  private City city;
}
