package com.atlantbh.cinemabh.validator.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

@Component
class EmailMxValidator {
  private final Logger logger = LoggerFactory.getLogger(EmailMxValidator.class);

  public boolean isValid(String domain) {
    try {
      Record[] records = new Lookup(domain, Type.MX).run();

      return records != null && records.length > 0;

    } catch (Exception e) {
      logger.warn("Could not fetch mx records.{}", e.getMessage());
      return false;
    }
  }
}
