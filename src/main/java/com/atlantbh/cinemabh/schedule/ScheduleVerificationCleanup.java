package com.atlantbh.cinemabh.schedule;

import com.atlantbh.cinemabh.config.properties.VerificationProperties;
import com.atlantbh.cinemabh.repository.VerificationCodeRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleVerificationCleanup {
  private final VerificationCodeRepository repository;
  private final VerificationProperties verificationProperties;

  @Transactional
  @Scheduled(fixedRateString = "${verification.schedule-delete}")
  public void cleanupExpiredCodes() {
    LocalDateTime threshold = LocalDateTime.now().minus(verificationProperties.getExpiration());

    int deleted = repository.deleteByCreatedAtBefore(threshold);

    log.info("deleted {} expired verification codes", deleted);
  }
}
