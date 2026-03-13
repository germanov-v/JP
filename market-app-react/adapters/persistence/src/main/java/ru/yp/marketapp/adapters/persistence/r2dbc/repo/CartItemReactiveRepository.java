package ru.yp.marketapp.adapters.persistence.r2dbc.repo;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import ru.yp.marketapp.adapters.persistence.entity.CartItemEntity;

public interface CartItemReactiveRepository extends ReactiveCrudRepository<CartItemEntity, Long> {

    @Query("""
        select * from market.cart_item
        where cart_id = :cartId and product_id = :productId
        """)
    Mono<CartItemEntity> findByCartAndProduct(long cartId, long productId);

    @Query("""
        select quantity from market.cart_item
        where cart_id = :cartId and product_id = :productId
        """)
    Mono<Integer> findQuantity(long cartId, long productId);

    @Query("""
        delete from market.cart_item
        where cart_id = :cartId and product_id = :productId
        """)
    Mono<Void> deleteByCartAndProduct(long cartId, long productId);
}
