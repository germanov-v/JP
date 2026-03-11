package ru.yp.marketapp.bootstrap.webV2.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.yp.marketapp.StartApplication;
import ru.yp.marketapp.adapters.persistence.entity.OrderEntity;
import ru.yp.marketapp.adapters.persistence.entity.OrderItemEntity;
import ru.yp.marketapp.adapters.persistence.entity.ProductEntity;
import ru.yp.marketapp.adapters.persistence.r2dbc.repo.OrderItemReactiveRepository;
import ru.yp.marketapp.adapters.persistence.r2dbc.repo.OrderReactiveRepository;
import ru.yp.marketapp.adapters.persistence.r2dbc.repo.ProductReactiveRepository;
import ru.yp.marketapp.bootstrap.webV2.PostgresConfig;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = {StartApplication.class, ProductControllerTests.TestConfig.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductControllerTests   extends PostgresConfig {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    OrderReactiveRepository orderReactiveRepository;

    @Autowired
    OrderItemReactiveRepository orderItemReactiveRepository;

    @Autowired
    ProductReactiveRepository productReactiveRepository;

    @BeforeEach
    void cleanup() {
        orderItemReactiveRepository.deleteAll().block();
        orderReactiveRepository.deleteAll().block();
        productReactiveRepository.deleteAll().block();
    }

    @Test
    void getOrdersItems() {
        seedOrder();

        webTestClient.get()
                .uri("/orders")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(body -> assertThat(body).isNotBlank());
    }

    @Test
    void getOrderById() {
        long orderId = seedOrder();

        webTestClient.get()
                .uri("/orders/{id}", orderId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(body -> assertThat(body).isNotBlank());
    }

    @Test
    void getNewOrder() {
        long orderId = seedOrder();

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/orders/{id}")
                        .queryParam("newOrder", "true")
                        .build(orderId))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(body -> assertThat(body).isNotBlank());
    }

    @Test
    void getOrderNotFound() {
        webTestClient.get()
                .uri("/orders/{id}", 999999)
                .exchange()
                .expectStatus().isNotFound();
    }

    private long seedOrder() {
        ProductEntity p = new ProductEntity();
        p.setTitle("P" + System.nanoTime());
        p.setDescription("D");
        p.setImgPath("/img.png");
        p.setPrice(150);
        p.setCount(10);

        ProductEntity savedProduct = productReactiveRepository.save(p).block();

        OrderEntity order = new OrderEntity();
        OrderEntity savedOrder = orderReactiveRepository.save(order).block();

        OrderItemEntity item = new OrderItemEntity();
        Assertions.assertNotNull(savedOrder);
        item.setOrderId(savedOrder.getId());
        item.setProductId(Objects.requireNonNull(savedProduct).getId());
        item.setQuantity(2);
        orderItemReactiveRepository.save(item).block();

        return savedOrder.getId();
    }
}
