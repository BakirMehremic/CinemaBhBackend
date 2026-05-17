package com.atlantbh.cinemabh.validator.email;

import static com.atlantbh.cinemabh.constant.AuthConstants.MX_RESOLVER_TIMEOUT_SECONDS;

import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.xbill.DNS.*;
import org.xbill.DNS.Record;

@Slf4j
@Component
class EmailMxValidator {
  public boolean isValid(String domain) {
    try {
      Resolver resolver = new SimpleResolver();
      resolver.setTimeout(Duration.ofSeconds(MX_RESOLVER_TIMEOUT_SECONDS));

      Lookup lookup = new Lookup(domain, Type.MX);
      lookup.setResolver(resolver);

      Record[] records = lookup.run();
      return records != null && records.length > 0;

    } catch (Exception e) {
      log.warn("Could not fetch mx records.{}", e.getMessage());
      return false;
    }
  }
}
