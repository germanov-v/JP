package ru.yp.marketapp.appplication.repositories;

import ru.yp.marketapp.appplication.model.CartActionEnum;

public interface CartRepository {

    boolean existsById(long cartId);
    void changeCount(long cartId, long productId, CartActionEnum action);

    long getOrCreateCartId();

    long createEmptyCart();

    int getQuantity(long cartId, long productId);

}
