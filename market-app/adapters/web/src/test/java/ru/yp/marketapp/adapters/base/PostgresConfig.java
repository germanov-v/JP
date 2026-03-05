package ru.yp.marketapp.adapters.base;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.yp.marketapp.adapters.persistence.entity.CartEntity;
import ru.yp.marketapp.adapters.persistence.jpa.repo.CartJpaRepository;
import ru.yp.marketapp.adapters.web.controllers.ItemControllerTests;


//@EnableJpaRepositories(basePackageClasses = CartJpaRepository.class)
public abstract class PostgresConfig {

    @Container
    static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:15-alpine")
                    .withDatabaseName("market_test")
                    .withUsername("postgres")
                    .withPassword("postgres");

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("market_test")
            .withUsername("postgres")
            .withPassword("postgres");
    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);

        //    registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    }

    @Configuration
    public static class TestConfig {

    }

}
