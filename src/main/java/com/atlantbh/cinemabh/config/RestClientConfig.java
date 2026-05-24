package com.atlantbh.cinemabh.config;

import static com.atlantbh.cinemabh.constant.RestClientConstants.CONNECTION_TIMEOUT_MS;
import static com.atlantbh.cinemabh.constant.RestClientConstants.READ_TIMEOUT_MS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestClientConfig {
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate(clientHttpRequestFactory());
  }

  private ClientHttpRequestFactory clientHttpRequestFactory() {
    SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
    factory.setConnectTimeout(CONNECTION_TIMEOUT_MS);
    factory.setReadTimeout(READ_TIMEOUT_MS);
    return factory;
  }
}
