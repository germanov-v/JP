package ru.yp.marketapp.adapters.persistence.r2dbc.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import ru.yp.marketapp.adapters.persistence.entity.OrderItemEntity;

public interface OrderItemReactiveRepository extends ReactiveCrudRepository<OrderItemEntity, Long> {

    Flux<OrderItemEntity> findByOrderId(Long orderId);
}
