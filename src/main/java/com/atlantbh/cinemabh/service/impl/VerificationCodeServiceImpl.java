package com.atlantbh.cinemabh.service.impl;

import static com.atlantbh.cinemabh.util.HashingUtils.toSha256;

import com.atlantbh.cinemabh.entity.VerificationCode;
import com.atlantbh.cinemabh.enums.VerificationType;
import com.atlantbh.cinemabh.repository.VerificationCodeRepository;
import com.atlantbh.cinemabh.service.VerificationCodeService;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerificationCodeServiceImpl implements VerificationCodeService {
  private final SecureRandom secureRandom = new SecureRandom();
  private final VerificationCodeRepository verificationCodeRepository;

  private String generateSixDigitCode() {
    return IntStream.range(0, 6)
        .mapToObj(i -> String.valueOf(secureRandom.nextInt(10)))
        .collect(Collectors.joining());
  }

  @Override
  public String generateAndSaveCode(Long userId, VerificationType type) {
    String code = generateSixDigitCode();

    VerificationCode codeEntity = new VerificationCode();
    codeEntity.setCodeHash(toSha256(code));
    codeEntity.setUserId(userId);
    codeEntity.setVerificationType(type);
    codeEntity.setCreatedAt(LocalDateTime.now());
    verificationCodeRepository.save(codeEntity);

    return code;
  }

  @Override
  public boolean isCodeValid(String code, Long userId, VerificationType type) {
    Optional<VerificationCode> storedCode =
        verificationCodeRepository.findByUserIdAndCodeHashAndVerificationType(
            userId, toSha256(code), type);

    if (storedCode.isPresent()) {
      verificationCodeRepository.delete(storedCode.get());
      return true;
    }

    return false;
  }
}
