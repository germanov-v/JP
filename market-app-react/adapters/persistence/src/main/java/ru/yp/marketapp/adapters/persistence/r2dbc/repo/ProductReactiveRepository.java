package ru.yp.marketapp.adapters.persistence.r2dbc.repo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.yp.marketapp.adapters.persistence.entity.ProductEntity;
import ru.yp.marketapp.domain.product.Product;

import java.util.UUID;

public interface ProductReactiveRepository extends ReactiveCrudRepository<ProductEntity,Long> {
    Mono<ProductEntity> findByGuidId(UUID guid);
}
