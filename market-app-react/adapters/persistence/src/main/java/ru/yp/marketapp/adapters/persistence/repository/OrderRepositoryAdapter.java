package ru.yp.marketapp.adapters.persistence.repository;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yp.marketapp.appplication.repositories.OrderRepository;
import ru.yp.marketapp.domain.order.Order;


@Repository
public class OrderRepositoryAdapter implements OrderRepository {

    // TODO: using DatabaseClient

    @Override
    public Flux<Order> findAll() {
        return null;
    }

    @Override
    public Mono<Order> findById(long id) {
        return null;
    }
}
