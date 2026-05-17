package com.atlantbh.cinemabh.schedule;

import com.atlantbh.cinemabh.repository.VerificationCodeRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleVerificationCleanup {
  private final VerificationCodeRepository repository;

  @Value("${verification.expiration-minutes}")
  private int expirationMinutes;

  @Transactional
  @Scheduled(fixedRateString = "${verification.schedule-delete-ms}")
  public void cleanupExpiredCodes() {
    LocalDateTime threshold = LocalDateTime.now().minusMinutes(expirationMinutes);

    int deleted = repository.deleteByCreatedAtBefore(threshold);

    log.info("deleted {} expired verification codes", deleted);
  }
}
