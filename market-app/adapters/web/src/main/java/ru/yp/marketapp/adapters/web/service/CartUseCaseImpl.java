package ru.yp.marketapp.adapters.web.service;

import org.springframework.stereotype.Service;
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
    public int getCount(long cartId,long itemId) {

        return cartRepository.getQuantity(cartId, itemId);
    }

    @Override
    public void changeCount(long cartId, long itemId, CartActionEnum action) {
        cartRepository.changeCount(cartId, itemId, action);
    }

    private long GetOrCreateCartId(Long cartId) {
        if (cartId == null) {
            return cartRepository.createEmptyCart();
        }
        if(!cartRepository.existsById(cartId)) {
            return cartRepository.createEmptyCart();
        }
        return cartId;
    }
}
