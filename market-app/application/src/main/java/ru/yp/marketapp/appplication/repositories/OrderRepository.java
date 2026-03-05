package ru.yp.marketapp.appplication.repositories;

import ru.yp.marketapp.domain.order.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    List<Order> findAll();

    Optional<Order> findById(long id);
}
