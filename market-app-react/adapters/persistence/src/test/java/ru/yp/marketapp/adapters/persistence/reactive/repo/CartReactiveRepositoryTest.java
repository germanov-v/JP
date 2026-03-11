package ru.yp.marketapp.adapters.persistence.reactive.repo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.yp.marketapp.adapters.persistence.r2dbc.repo.CartReactiveRepository;
import ru.yp.marketapp.adapters.persistence.repository.CartRepositoryAdapter;
import ru.yp.marketapp.appplication.repositories.CartRepository;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@ComponentScan(basePackages = "ru.yp.marketapp.adapters.persistence")
@ContextConfiguration(classes = CartReactiveRepositoryTest.TestConfig.class)
public class CartReactiveRepositoryTest {



    @Autowired
    private CartRepository cartRepositoryAdapter;

    @Autowired
    private CartReactiveRepository cartReactiveRepository;

    @Configuration
    static class TestConfig {
    }


    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("market_test")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void configureTestProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.r2dbc.url", () ->
                //        "postgresql://%s:%d/%s"
                "r2dbc:postgresql://%s:%d/%s".formatted(
                        postgres.getHost(),
                        postgres.getMappedPort(5432),
                        postgres.getDatabaseName()
                ));
        registry.add("spring.r2dbc.username", postgres::getUsername);
        registry.add("spring.r2dbc.password", postgres::getPassword);

        registry.add("spring.liquibase.url", postgres::getJdbcUrl);
        registry.add("spring.liquibase.user", postgres::getUsername);
        registry.add("spring.liquibase.password", postgres::getPassword);
    }

    @BeforeEach
    void cleanup() {
        // https://projectreactor.io/docs/test/release/api/reactor/test/StepVerifier.html
        StepVerifier.create(
                Mono.when(
                        cartRepositoryAdapter.deleteAllCartItems(),
                        cartRepositoryAdapter.deleteAllCarts()
                )
        ).verifyComplete();
    }

    @Test
    void findPageIds_returnsIdsInDescOrder_andHasTotalCount() {
        Mono<Long> scenario = cartRepositoryAdapter.createEmptyCart()
                .then(cartRepositoryAdapter.createEmptyCart())
                .then(cartRepositoryAdapter.createEmptyCart())
                .thenMany(cartReactiveRepository.findPageIds(0, 2))
                .collectList()
                .zipWith(cartReactiveRepository.countAll())
                .map(tuple -> {
                    var ids = tuple.getT1();
                    var total = tuple.getT2();

                    org.assertj.core.api.Assertions.assertThat(ids).hasSize(2);
                    org.assertj.core.api.Assertions.assertThat(ids.get(0)).isGreaterThan(ids.get(1));
                    org.assertj.core.api.Assertions.assertThat(total).isGreaterThanOrEqualTo(3L);

                    return total;
                });

        StepVerifier.create(scenario)
                .expectNextMatches(total -> total >= 3)
                .verifyComplete();
    }
}
