package com.atlantbh.cinemabh.repository;

import com.atlantbh.cinemabh.entity.VerificationCode;
import com.atlantbh.cinemabh.enums.VerificationType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
  Optional<VerificationCode> findByUserIdAndCodeHashAndVerificationType(
      Long userId, String codeHash, VerificationType verificationType);
}
