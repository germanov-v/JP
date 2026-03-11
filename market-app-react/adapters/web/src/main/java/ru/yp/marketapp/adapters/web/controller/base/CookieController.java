package ru.yp.marketapp.adapters.web.controller.base;


import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpResponse;

public interface CookieController {

    String CART_COOKIE = "cartId";

    default void setCartCookie(ServerHttpResponse response, long cartId, Long cartIdCookieId) {
        if (cartIdCookieId != null && cartIdCookieId == cartId) {
            return;
        }

        var cookie = ResponseCookie.from(CART_COOKIE, Long.toString(cartId))
                .httpOnly(true)
                .path("/")
                .maxAge(java.time.Duration.ofHours(30))
                .build();

        response.addCookie(cookie);
    }
}
