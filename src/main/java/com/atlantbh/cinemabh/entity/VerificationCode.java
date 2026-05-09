package com.atlantbh.cinemabh.entity;

import com.atlantbh.cinemabh.enums.VerificationType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Setter
@Table(name = "verification_codes")
public class VerificationCode {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "type", columnDefinition = "verification_type", nullable = false)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  private VerificationType verificationType;

  @Column(nullable = false)
  private String codeHash;
}
