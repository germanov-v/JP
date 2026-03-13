package ru.yp.marketapp.adapters.persistence.r2dbc.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import ru.yp.marketapp.adapters.persistence.entity.OrderEntity;

public interface OrderReactiveRepository extends ReactiveCrudRepository<OrderEntity, Long> {
}
