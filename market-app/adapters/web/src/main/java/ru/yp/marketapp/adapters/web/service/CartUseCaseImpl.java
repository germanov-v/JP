package ru.yp.marketapp.adapters.web.service;

import org.springframework.stereotype.Service;
import ru.yp.marketapp.adapters.web.service.base.CartUseCase;
import ru.yp.marketapp.adapters.web.view.CartActionView;

@Service
public class CartUseCaseImpl implements CartUseCase {
    @Override
    public int getCount(long itemId) {
        return 0;
    }

    @Override
    public void changeCount(long itemId, CartActionView action) {

    }
}
