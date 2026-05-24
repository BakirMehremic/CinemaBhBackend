package com.atlantbh.cinemabh.service;

import com.atlantbh.cinemabh.enums.VerificationType;

public interface VerificationCodeService {
  String generateAndSaveCode(Long userId, VerificationType type);

  boolean isCodeValid(String code, Long userId, VerificationType type);

  boolean isCodeSentAndValid(Long userId, VerificationType type);
}
