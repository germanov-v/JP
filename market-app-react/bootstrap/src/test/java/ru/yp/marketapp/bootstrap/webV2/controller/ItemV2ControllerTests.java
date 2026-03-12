package ru.yp.marketapp.bootstrap.webV2.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import ru.yp.marketapp.StartApplication;
import ru.yp.marketapp.adapters.persistence.entity.ProductEntity;
import ru.yp.marketapp.adapters.persistence.r2dbc.repo.CartItemReactiveRepository;
import ru.yp.marketapp.adapters.persistence.r2dbc.repo.ProductReactiveRepository;
import ru.yp.marketapp.bootstrap.webV2.PostgresConfig;

import static org.assertj.core.api.Assertions.assertThat;


// mvn -pl bootstrap -am -Dtest=ItemV2ControllerTests,OrdersControllerTests test
// mvn -pl bootstrap -am -Dtest=ItemV2ControllerTests,OrdersControllerTests -Dsurefire.failIfNoSpecifiedTests=false test

@SpringBootTest(classes = {StartApplication.class,
        ItemV2ControllerTests.TestConfig.class},
webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class ItemV2ControllerTests extends PostgresConfig {

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    ProductReactiveRepository productReactiveRepository;

    @Autowired
    CartItemReactiveRepository cartItemReactiveRepository;

    @BeforeEach
    void cleanup() {
        cartItemReactiveRepository.deleteAll().block();
        productReactiveRepository.deleteAll().block();
    }

    @Test
    void getItemSetCookieRenderView() {
        long productId = seedProduct();

        webTestClient.get()
                .uri("/items/{id}", productId)
                .exchange()
                .expectStatus().isOk()
                .expectCookie().exists("cartId")
                .expectBody(String.class)
                .value(body -> assertThat(body).isNotBlank());
    }

    @Test
    void itemPlusSetCookie() {
        long productId = seedProduct();

        webTestClient.post()
                .uri("/items/{id}", productId)
                // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/MediaType.html
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                // .bodyValue("action=PLUS")

                .body(BodyInserters.fromFormData("action", "PLUS"))
              //  .header("Content-Type", "application/x-www-form-urlencoded")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("Location", "/items/" + productId)
                .expectCookie().exists("cartId");
    }

    @Test
    void getItem404() {
        webTestClient.get()
                .uri("/items/{id}", 999999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void getItems() {
        seedProducts(3);

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/items")
                        .queryParam("search", "")
                        .queryParam("sort", "NO")
                        .queryParam("pageSize", "10")
                        .queryParam("pageNumber", "1")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectCookie().exists("cartId")
                .expectBody(String.class)
                .value(body -> assertThat(body).isNotBlank());
    }

    @Test
    void postItemsPlusCreateCartRedirect() {
        long productId = seedProduct();

        var url = """
                        id=%d&action=PLUS&search=abc&sort=NO&pageSize=10&pageNumber=2
                        """.formatted(productId).replace("\n", "");

        String cartId = webTestClient.post()
                .uri("/items")
                .bodyValue(url)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueMatches("Location", "/items\\?search=abc&sort=NO&pageSize=10&pageNumber=2.*")
                .expectCookie().exists("cartId")
                .returnResult(String.class)
                .getResponseCookies()
                .getFirst("cartId")
                .getValue();

        var opt = cartItemReactiveRepository.findByCartAndProduct(Long.parseLong(cartId), productId).block();

        assertThat(opt).isNotNull();
        assertThat(opt.getQuantity()).isEqualTo(1);
    }

    private void seedProducts(int n) {
        for (int i = 0; i < n; i++) {
            seedProduct();
        }
    }

    private long seedProduct() {
        ProductEntity p = new ProductEntity();
        p.setTitle("P" + System.nanoTime());
        p.setDescription("D");
        p.setImgPath("/img.png");
        p.setPrice(100);
        p.setCount(10);

        return productReactiveRepository.save(p).block().getId();
    }

}
