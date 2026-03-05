package ru.yp.marketapp.adapters.web.service.base;

import ru.yp.marketapp.appplication.model.CartActionEnum;

public interface CartUseCase {
    int getCount(long itemId);
    void changeCount(long itemId, CartActionEnum action);
}