package com.xchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/** Spring boot application start point */
@EnableCaching
@Configuration
@SpringBootApplication
public class XChangeApplication {

  /**
   * start point
   *
   * @param args system arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(XChangeApplication.class, args);
  }

  @Bean
  public RestTemplate getRestTemplate() {
    return new RestTemplate();
  }
}
