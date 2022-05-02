package com.example.precharge;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

public class DatabaseITInitializer
    implements ApplicationContextInitializer<ConfigurableApplicationContext> {

  private static final PostgreSQLContainer<?> POSTGRE_SQL_CONTAINER =
      new PostgreSQLContainer<>("postgres:12.8")
          .withDatabaseName("precharge")
          .withUsername("precharge_user")
          .withPassword("precharge");

  static {
    POSTGRE_SQL_CONTAINER.start();
  }

  @Override
  public void initialize(ConfigurableApplicationContext applicationContext) {
    TestPropertyValues.of(
            "spring.datasource.url=" + POSTGRE_SQL_CONTAINER.getJdbcUrl(),
            "spring.datasource.username=" + POSTGRE_SQL_CONTAINER.getUsername(),
            "spring.datasource.password=" + POSTGRE_SQL_CONTAINER.getPassword())
        .applyTo(applicationContext.getEnvironment());
  }
}
