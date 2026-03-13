package ru.yp.marketapp.adapters.web.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yp.marketapp.adapters.web.service.base.CartUseCase;
import ru.yp.marketapp.appplication.model.CartActionEnum;
import ru.yp.marketapp.appplication.repositories.CartRepository;

@Service
public class CartUseCaseImpl implements CartUseCase {

    private final CartRepository cartRepository;

    public CartUseCaseImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public Mono<Integer> getCount(long cartId, long itemId) {
        return cartRepository.getQuantity(cartId, itemId);
    }

    @Override
    public Mono<Void> changeCount(long cartId, long itemId, CartActionEnum action) {
        return cartRepository.changeCount(cartId, itemId, action);
    }

    @Override
    public Mono<Long> getOrCreateCartId(Long cartId) {
        if (cartId == null) {
            return cartRepository.createEmptyCart();
        }

        return cartRepository.existsById(cartId)
                .flatMap(exists -> exists
                        ? Mono.just(cartId)
                        : cartRepository.createEmptyCart());
    }
}
