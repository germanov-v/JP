package ru.yp.marketapp.adapters.persistence.r2dbc.repo;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yp.marketapp.adapters.persistence.entity.CartEntity;

public interface CartReactiveRepository extends ReactiveCrudRepository<CartEntity, Long> {
    Mono<CartEntity> findFirstByOrderByIdAsc();



    @Query("""
        select id
        from market.cart
        order by id desc
        limit :limit offset :offset
        """)
    Flux<Long> findPageIds(@Param("offset") int offset, @Param("limit") int limit);

    @Query("""
        select count(*)
        from market.cart
        """)
    Mono<Long> countAll();
}