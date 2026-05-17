package com.atlantbh.cinemabh.service.impl;

import static com.atlantbh.cinemabh.util.HashingUtils.toSha256;

import com.atlantbh.cinemabh.entity.User;
import com.atlantbh.cinemabh.entity.VerificationCode;
import com.atlantbh.cinemabh.enums.AuthEventOutcome;
import com.atlantbh.cinemabh.enums.AuthEventType;
import com.atlantbh.cinemabh.enums.VerificationType;
import com.atlantbh.cinemabh.logging.AuthEvent;
import com.atlantbh.cinemabh.repository.UserRepository;
import com.atlantbh.cinemabh.repository.VerificationCodeRepository;
import com.atlantbh.cinemabh.service.VerificationCodeService;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerificationCodeServiceImpl implements VerificationCodeService {
  private final SecureRandom secureRandom = new SecureRandom();
  private final VerificationCodeRepository verificationCodeRepository;
  private final UserRepository userRepository;

  @Value("${verification.expiration-minutes}")
  private int expirationMinutes;

  private String generateSixDigitCode() {
    return IntStream.range(0, 6)
        .mapToObj(i -> String.valueOf(secureRandom.nextInt(10)))
        .collect(Collectors.joining());
  }

  @Override
  @Transactional
  public String generateAndSaveCode(Long userId, VerificationType type) {
    String code = generateSixDigitCode();
    User user = userRepository.getReferenceById(userId);

    VerificationCode codeEntity = new VerificationCode();
    codeEntity.setCodeHash(toSha256(code));
    codeEntity.setUser(user);
    codeEntity.setVerificationType(type);
    codeEntity.setCreatedAt(LocalDateTime.now());
    codeEntity.setExpiresAt(LocalDateTime.now().plusMinutes(expirationMinutes));

    verificationCodeRepository.save(codeEntity);

    log.info(
        "{}",
        AuthEvent.builder(AuthEventType.ISSUE_CODE, AuthEventOutcome.SUCCESS)
            .userId(userId)
            .build());
    return code;
  }

  @Override
  public boolean isCodeValid(String code, Long userId, VerificationType type) {
    Optional<VerificationCode> storedCode =
        verificationCodeRepository.findByUserIdAndCodeHashAndVerificationType(
            userId, toSha256(code), type);

    if (storedCode.isEmpty()) {
      log.info(
          "{}",
          AuthEvent.builder(AuthEventType.VALIDATE_CODE, AuthEventOutcome.FAILURE)
              .userId(userId)
              .detail("User tried to verify with nonexistent code")
              .build());
      return false;
    }

    if (storedCode.get().getExpiresAt().isAfter(LocalDateTime.now())) {
      log.info(
          "{}",
          AuthEvent.builder(AuthEventType.VALIDATE_CODE, AuthEventOutcome.FAILURE)
              .userId(userId)
              .detail("User tried to verify with expired code")
              .build());
      return false;
    }

    verificationCodeRepository.delete(storedCode.get());
    log.info(
        "{}",
        AuthEvent.builder(AuthEventType.VALIDATE_CODE, AuthEventOutcome.SUCCESS)
            .userId(userId)
            .build());
    return true;
  }
}
