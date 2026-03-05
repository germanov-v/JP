package ru.yp.marketapp.adapters.web.demo;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
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

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@EnableJpaRepositories(basePackageClasses = CartJpaRepository.class)
@EntityScan(basePackageClasses = CartEntity.class)
@ActiveProfiles("test") //("prod")
// полная подмена и всегда. не смотрим датасорс и его конфигурацию (а ее и нет)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = CartJpaRepositoryWeb.TestConfig.class)
// Liquibase up ?
//@ImportAutoConfiguration(LiquibaseAutoConfiguration.class)
public class CartJpaRepositoryWeb {

    @Autowired
    CartJpaRepository cartJpaRepository;

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("market_test")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void configureTestProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    }

    @Configuration
    static class TestConfig {
        // Пустая конфигурация для теста
    }


    @Test
    public void test() {
        assertThat(3).isGreaterThanOrEqualTo(2);
    }



    @Test
    public void findPageIds_returnsIdsInDescOrder_andHasTotalCount() {
        var c1 = cartJpaRepository.save(new CartEntity());
        var c2 = cartJpaRepository.save(new CartEntity());
        var c3 = cartJpaRepository.save(new CartEntity());

        // Act
        var page = cartJpaRepository.findPageIds(PageRequest.of(0, 2));

        assertThat(page.getContent()).containsExactly(c3.getId(), c2.getId());
        assertThat(page.getTotalElements()).isGreaterThanOrEqualTo(3);
        assertThat(page.getTotalPages()).isGreaterThanOrEqualTo(2);

    }
}
