package com.atlantbh.cinemabh.service;

public interface EmailSendingService {
  void sendVerificationEmail(String recipient, String subject, String code);
}
