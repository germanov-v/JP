package ru.yp.marketapp.bootstrap.webV2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.yp.marketapp.BootstrapApplication;
import ru.yp.marketapp.adapters.persistence.jpa.repo.ProductJpaRepository;
import ru.yp.marketapp.adapters.persistence.repository.ProductRepositoryAdapter;
import ru.yp.marketapp.appplication.repositories.ProductRepository;

import ru.yp.marketapp.adapters.persistence.entity.ProductEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Testcontainers
@SpringBootTest(classes = {BootstrapApplication.class, ItemV2ControllerTests.TestConfig.class})
//@DataJpaTest // ЭТО ПЛОХОЙ ВАРИАНТ С SpringBootTest]р
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@Import( ProductRepositoryAdapter.class)
//@EnableJpaRepositories(basePackages = {"ru.yp.marketapp.adapters.persistence"})
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ItemV2ControllerTests {

    @Container
    static final PostgreSQLContainer<?> postgresContainer =
            new PostgreSQLContainer<>("postgres:15-alpine")
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



    @Autowired
    MockMvc mvc;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductJpaRepository productJpaRepository;

  //  @Test

    public void getItemSetCookieRenderView() throws Exception {
        long productId = seedProduct();

        mvc.perform(get("/items/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(view().name("item"))
                .andExpect(model().attributeExists("item"))
                //
                .andExpect(cookie().exists("cartId"));
    }

  //  @Test
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
