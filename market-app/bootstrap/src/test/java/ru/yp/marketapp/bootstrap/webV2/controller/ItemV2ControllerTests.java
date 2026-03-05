package ru.yp.marketapp.bootstrap.webV2.controller;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.yp.marketapp.BootstrapApplication;
import ru.yp.marketapp.adapters.persistence.jpa.repo.ProductJpaRepository;
import ru.yp.marketapp.adapters.persistence.jpa.repo.CartItemJpaRepository;
import ru.yp.marketapp.adapters.persistence.repository.ProductRepositoryAdapter;
import ru.yp.marketapp.appplication.repositories.ProductRepository;

import ru.yp.marketapp.adapters.persistence.entity.ProductEntity;
import ru.yp.marketapp.bootstrap.webV2.PostgresConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


//  ./gradlew :bootstrap:test --tests "*ItemV2*" --info
//@Testcontainers
@SpringBootTest(classes = {BootstrapApplication.class, ItemV2ControllerTests.TestConfig.class})
//@DataJpaTest // ЭТО ПЛОХОЙ ВАРИАНТ С SpringBootTest]р
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Import( ProductRepositoryAdapter.class)
//@EnableJpaRepositories(basePackages = {"ru.yp.marketapp.adapters.persistence"})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ItemV2ControllerTests extends PostgresConfig {

//    @Container
//    static final PostgreSQLContainer<?> postgresContainer =
//            new PostgreSQLContainer<>("postgres:15-alpine")
//                    .withDatabaseName("market_test")
//                    .withUsername("postgres")
//                    .withPassword("postgres");
//
//
//    @DynamicPropertySource
//    static void properties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
//        registry.add("spring.datasource.username", postgresContainer::getUsername);
//        registry.add("spring.datasource.password", postgresContainer::getPassword);
//
//        //    registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
//        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
//    }
//
//    @Configuration
//    public static class TestConfig {
//
//    }



    @Autowired
    MockMvc mvc;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductJpaRepository productJpaRepository;

    @Autowired
    CartItemJpaRepository cartItemJpaRepository;

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

    @Test
    public void test() {
        assertThat(3).isGreaterThanOrEqualTo(2);
    }


    @Test
    void getItems() throws Exception {
        seedProducts(3);

        mvc.perform(get("/items")
                        .param("search", "")
                        .param("sort", "NO")
                        .param("pageSize", "10")
                        .param("pageNumber", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("items"))
                .andExpect(model().attributeExists("items"))
                .andExpect(model().attributeExists("paging"))
                .andExpect(cookie().exists("cartId"));
    }

    @Test
    @Transactional
    void postItemsPlusCreateCartRedirect() throws Exception {
        long productId = seedProduct();

        var res = mvc.perform(post("/items")
                        .param("id", Long.toString(productId))
                        .param("action", "PLUS")
                        .param("search", "abc")
                        .param("sort", "NO")
                        .param("pageSize", "10")
                        .param("pageNumber", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/items?search=abc&sort=NO&pageSize=10&pageNumber=2*"))
                .andExpect(cookie().exists("cartId"))
                .andReturn();

         var cookie = res.getResponse().getCookie("cartId");
        String cartId = cookie.getValue();

        var opt = cartItemJpaRepository.findByCartAndProduct(Long.parseLong(cartId), productId);
        assertThat(opt).isPresent();
        assertThat(opt.get().getQuantity()).isEqualTo(1);
    }

    private void seedProducts(int n) {
        for (int i = 0; i < n; i++) seedProduct();
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
