package ru.yp.marketapp.adapters.persistence.r2dbc.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.yp.marketapp.adapters.persistence.entity.CartEntity;

public interface CartReactiveRepository extends ReactiveCrudRepository<CartEntity, Long> {
    Mono<CartEntity> findFirstByOrderByIdAsc();
}