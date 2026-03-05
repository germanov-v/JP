package ru.yp.marketapp.adapters.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.yp.marketapp.adapters.base.PostgresConfig;
import ru.yp.marketapp.adapters.persistence.entity.CartEntity;
import ru.yp.marketapp.adapters.persistence.entity.ProductEntity;
import ru.yp.marketapp.adapters.persistence.jpa.repo.CartJpaRepository;
import ru.yp.marketapp.adapters.persistence.jpa.repo.ProductJpaRepository;
import ru.yp.marketapp.appplication.repositories.ProductRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc

@EntityScan(basePackageClasses = CartEntity.class)
@EnableJpaRepositories(basePackageClasses = CartJpaRepository.class)

@ActiveProfiles("test") //("prod")
//// полная подмена и всегда. не смотрим датасорс и его конфигурацию (а ее и нет)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class ItemControllerTests extends PostgresConfig {

    @Autowired
    MockMvc mvc;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductJpaRepository productJpaRepository;

    @Test

    public void getItemSetCookieRenderView() throws Exception {
        long productId = seedProduct();

        mvc.perform(get("/items/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(view().name("item"))
                .andExpect(model().attributeExists("item"))
                //
                .andExpect(cookie().exists("cartId"));
    }

    @Test
    public void itemPlusSetCookie() throws Exception {
        long productId = seedProduct();

        mvc.perform(post("/items/{id}", productId)
                        .param("action", "PLUS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items/" + productId))
                .andExpect(cookie().exists("cartId"));
    }

    @Test
    public void getItem404() throws Exception {
        mvc.perform(get("/items/{id}", 999999))
                .andExpect(status().isNotFound());
    }

    private long seedProduct() {
        ProductEntity p = new ProductEntity();
        p.setTitle("P" + System.nanoTime());
        p.setDescription("D");
        p.setImgPath("/img.png");
        p.setPrice(100);
        p.setCount(10);
      return productJpaRepository.save(p).getId();
      //  throw new UnsupportedOperationException("not implemented");
    }

}
