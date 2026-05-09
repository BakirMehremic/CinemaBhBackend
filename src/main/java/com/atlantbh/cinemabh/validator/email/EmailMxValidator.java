package com.atlantbh.cinemabh.validator.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

@Slf4j
@Component
class EmailMxValidator {
  public boolean isValid(String domain) {
    try {
      Record[] records = new Lookup(domain, Type.MX).run();

      return records != null && records.length > 0;

    } catch (Exception e) {
      log.warn("Could not fetch mx records.{}", e.getMessage());
      return false;
    }
  }
}
