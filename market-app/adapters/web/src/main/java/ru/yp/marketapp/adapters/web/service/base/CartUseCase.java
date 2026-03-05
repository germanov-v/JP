package ru.yp.marketapp.adapters.web.service.base;

import ru.yp.marketapp.appplication.model.CartActionEnum;

public interface CartUseCase {
    int getCount(long cartId,long itemId);
    void changeCount(long cartId, long itemId, CartActionEnum action);

    long GetOrCreateCartId(Long cartId);
}