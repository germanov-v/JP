package ru.yp.marketapp.bootstrap.webV2.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.yp.marketapp.BootstrapApplication;
import ru.yp.marketapp.adapters.persistence.jpa.repo.ProductJpaRepository;
import ru.yp.marketapp.adapters.persistence.jpa.repo.OrderJpaRepository;
import ru.yp.marketapp.adapters.persistence.repository.ProductRepositoryAdapter;
import ru.yp.marketapp.appplication.repositories.ProductRepository;

import ru.yp.marketapp.adapters.persistence.entity.ProductEntity;
import ru.yp.marketapp.adapters.persistence.entity.OrderEntity;
import ru.yp.marketapp.adapters.persistence.entity.OrderItemEntity;
import ru.yp.marketapp.bootstrap.webV2.PostgresConfig;


import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(classes = {BootstrapApplication.class, ItemV2ControllerTests.TestConfig.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProductControllerTests   extends PostgresConfig {

    @Autowired
    MockMvc mvc;

    @Autowired
    OrderJpaRepository orderJpaRepository;

    @Autowired
    ProductJpaRepository productJpaRepository;

    @Test
    void getOrdersItems() throws Exception {
        seedOrder();

        mvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"))
                .andExpect(model().attributeExists("orders"));
    }

    @Test
    void getOrderById() throws Exception {
        long orderId = seedOrder();

        mvc.perform(get("/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(view().name("order"))
                .andExpect(model().attributeExists("order"))
                .andExpect(model().attributeExists("newOrder"));
    }

    @Test
    void getNewOrder() throws Exception {
        long orderId = seedOrder();

        mvc.perform(get("/orders/{id}", orderId).param("newOrder", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("order"))
                .andExpect(model().attribute("newOrder", true));
    }

    @Test
    void getOrderNotFound() throws Exception {
        mvc.perform(get("/orders/{id}", 999999))
                .andExpect(status().isNotFound());
    }

    private long seedOrder() {
        ProductEntity p = new ProductEntity();
        p.setTitle("P" + System.nanoTime());
        p.setDescription("D");
        p.setImgPath("/img.png");
        p.setPrice(150);
        p.setCount(10);
        p = productJpaRepository.save(p);

        OrderEntity order = new OrderEntity();

        OrderItemEntity item = new OrderItemEntity();
        item.setOrder(order);
        item.setProduct(p);
        item.setQuantity(2);

        order.setItems(List.of(item));

        return orderJpaRepository.save(order).getId();
    }
}
