package ru.yp.marketapp.adapters.web.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yp.marketapp.adapters.web.service.base.OrdersQuery;
import ru.yp.marketapp.adapters.web.view.OrderView;
import ru.yp.marketapp.appplication.repositories.OrderRepository;

import java.util.List;
import java.util.Optional;

@Service
public class OrdersQueryImpl implements OrdersQuery {

    private final OrderRepository orderRepository;

    public OrdersQueryImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Flux<OrderView> findAll() {
        return orderRepository.findAll()
                .map(OrderView::fromDomain);
    }

    @Override
    public Mono<OrderView> findById(long id) {
        return orderRepository.findById(id)
                .map(OrderView::fromDomain);
    }
}
