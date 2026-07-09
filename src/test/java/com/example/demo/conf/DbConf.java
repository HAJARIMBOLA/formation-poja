package com.example.demo.conf;

import com.example.demo.PojaGenerated;
import java.time.Duration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

/**
 * Spins up a disposable Postgres container (via Testcontainers) so that integration tests run
 * against a real database, with Flyway migrations applied automatically by Spring Boot.
 */
@PojaGenerated
public class DbConf {

  private static final PostgreSQLContainer<?> POSTGRES =
      new PostgreSQLContainer<>(DockerImageName.parse("postgres:16-alpine"))
          .waitingFor(Wait.forListeningPort().withStartupTimeout(Duration.ofMinutes(3)))
          .withStartupTimeout(Duration.ofMinutes(3));

  public void configureProperties(DynamicPropertyRegistry registry) {
    POSTGRES.start();
    registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
    registry.add("spring.datasource.username", POSTGRES::getUsername);
    registry.add("spring.datasource.password", POSTGRES::getPassword);
  }
}
