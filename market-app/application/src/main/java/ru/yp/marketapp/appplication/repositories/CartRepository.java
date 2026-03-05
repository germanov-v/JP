package ru.yp.marketapp.appplication.repositories;

public interface CartRepository {
    void changeCount(long cartId, long productId, long count);
}
