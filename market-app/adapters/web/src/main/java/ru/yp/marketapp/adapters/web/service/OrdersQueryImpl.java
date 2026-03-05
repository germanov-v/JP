package ru.yp.marketapp.adapters.web.service;

import org.springframework.stereotype.Service;
import ru.yp.marketapp.adapters.web.service.base.OrdersQuery;
import ru.yp.marketapp.adapters.web.view.OrderView;

import java.util.List;
import java.util.Optional;

@Service
public class OrdersQueryImpl implements OrdersQuery {
    @Override
    public List<OrderView> findAll() {
        return List.of();
    }

    @Override
    public Optional<OrderView> findById(long id) {
        return Optional.empty();
    }
}
