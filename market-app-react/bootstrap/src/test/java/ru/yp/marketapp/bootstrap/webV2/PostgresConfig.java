package ru.yp.marketapp.bootstrap.webV2;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@Testcontainers
//@SpringBootTest(classes = {BootstrapApplication.class, ItemV2ControllerTests.TestConfig.class})
public abstract class PostgresConfig {

    @Container
    static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:16")
                    .withDatabaseName("market_test")
                    .withUsername("postgres")
                    .withPassword("postgres");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", () ->
                "r2dbc:postgresql://%s:%d/%s".formatted(
                        postgresContainer.getHost(),
                        postgresContainer.getMappedPort(5432),
                        postgresContainer.getDatabaseName()
                )
        );
        registry.add("spring.r2dbc.username", postgresContainer::getUsername);
        registry.add("spring.r2dbc.password", postgresContainer::getPassword);

        registry.add("spring.liquibase.url", postgresContainer::getJdbcUrl);
        registry.add("spring.liquibase.user", postgresContainer::getUsername);
        registry.add("spring.liquibase.password", postgresContainer::getPassword);
    }

    @Configuration
    public static class TestConfig {

    }
}
