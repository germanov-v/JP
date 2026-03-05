package ru.yp.marketapp.adapters.web.service.base;



import ru.yp.marketapp.adapters.web.view.OrderView;

import java.util.List;
import java.util.Optional;

public interface OrdersQuery {
    List<OrderView> findAll();
    Optional<OrderView> findById(long id);
}