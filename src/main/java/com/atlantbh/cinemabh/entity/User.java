package com.atlantbh.cinemabh.entity;

import static com.atlantbh.cinemabh.constant.AuthConstants.PASSWORD_HASH_LENGTH;

import com.atlantbh.cinemabh.enums.UserRole;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(AccessLevel.NONE)
  private Long id;

  @Column(name = "role", nullable = false, columnDefinition = "user_role")
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  private UserRole userRole;

  @Column(nullable = false, length = PASSWORD_HASH_LENGTH)
  private String passwordHash;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  @Column private String street;

  @Column private String phoneNumber;

  @Column(unique = true, nullable = false)
  private String email;

  @Column private String imagePath;

  @CreationTimestamp
  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private boolean verified;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "city_id")
  private City city;
}
