package ru.yp.marketapp.adapters.web.service.base;

import reactor.core.publisher.Mono;
import ru.yp.marketapp.appplication.model.CartActionEnum;

public interface CartUseCase {
    Mono<Integer> getCount(long cartId, long itemId);
    Mono<Void> changeCount(long cartId, long itemId, CartActionEnum action);
    Mono<Long> getOrCreateCartId(Long cartId);
}