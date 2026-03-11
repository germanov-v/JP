package ru.yp.marketapp.appplication.repositories;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yp.marketapp.domain.order.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Flux<Order> findAll();

    Mono<Order> findById(long id);
}
