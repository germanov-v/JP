package ru.yp.marketapp.adapters.web.service.base;

import ru.yp.marketapp.adapters.web.view.CartActionView;

public interface CartUseCase {
    int getCount(long itemId);
    void changeCount(long itemId, CartActionView action);
}