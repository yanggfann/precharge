package com.example.precharge.client;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

@Slf4j
@AllArgsConstructor
public class RestClient {

  private final RestTemplate restTemplate;
  private final String baseUrl;

  public <T> T post(String endpoint, Object request, Class<T> responseClazz) {
    log.info("Start invoke {} url", endpoint);
    return restTemplate.postForObject(baseUrl + endpoint, request, responseClazz);
  }
}
