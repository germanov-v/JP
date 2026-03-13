package ru.yp.marketapp.appplication.repositories;

import reactor.core.publisher.Mono;
import ru.yp.marketapp.appplication.model.CartActionEnum;

public interface CartRepository {

    Mono<Boolean> existsById(long cartId);
    Mono<Void> changeCount(long cartId, long productId, CartActionEnum action);

    Mono<Long> getOrCreateCartId();

    Mono<Long> createEmptyCart();

    Mono<Integer> getQuantity(long cartId, long productId);

    Mono<Void> deleteAllCartItems();

    Mono<Void> deleteAllCarts();

}
