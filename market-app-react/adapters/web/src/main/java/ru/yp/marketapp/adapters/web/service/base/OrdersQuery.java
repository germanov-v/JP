package ru.yp.marketapp.adapters.web.service.base;



import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yp.marketapp.adapters.web.view.OrderView;

import java.util.List;
import java.util.Optional;

public interface OrdersQuery {
    Flux<OrderView> findAll();
    Mono<OrderView> findById(long id);
}