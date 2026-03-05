package ru.yp.marketapp.appplication.repositories;

import ru.yp.marketapp.appplication.model.CartActionEnum;

public interface CartRepository {
    void changeCount(long cartId, long productId, CartActionEnum action);
}
