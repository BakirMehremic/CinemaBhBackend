package com.atlantbh.cinemabh.repository;

import com.atlantbh.cinemabh.entity.VerificationCode;
import com.atlantbh.cinemabh.enums.VerificationType;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
  Optional<VerificationCode> findByUserIdAndCodeHashAndVerificationType(
      Long userId, String codeHash, VerificationType verificationType);

  @Modifying
  @Query("""
        delete from VerificationCode v
        where v.createdAt < :threshold
    """)
  int deleteAllOlderThan(@Param("threshold") LocalDateTime threshold);
}
