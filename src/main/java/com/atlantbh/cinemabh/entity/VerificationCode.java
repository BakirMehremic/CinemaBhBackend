package com.atlantbh.cinemabh.entity;

import static com.atlantbh.cinemabh.constant.AuthConstants.VERIFICATION_CODE_HASH_LENGTH;

import com.atlantbh.cinemabh.enums.VerificationType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
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
  @Setter(AccessLevel.NONE)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime expiresAt;

  @Column(name = "type", columnDefinition = "verification_type", nullable = false)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  private VerificationType verificationType;

  @Column(nullable = false, length = VERIFICATION_CODE_HASH_LENGTH)
  private String codeHash;
}
