package com.example.precharge.config;

import com.example.precharge.client.RestClient;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@AllArgsConstructor
public class RestClientConfig {
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  RestClient thirdPaymentRestClient(@Value("${third.payment.base.url}") String baseUrl) {
    return new RestClient(restTemplate(), baseUrl);
  }
}
