package ru.yp.marketapp.adapters.persistence.jpa.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yp.marketapp.adapters.persistence.entity.CartItemEntity;

import java.util.Optional;

public interface CartItemJpaRepository extends JpaRepository<CartItemEntity, Long> {

    @Query("""
        select ci from CartItemEntity ci
        where ci.cart.id = :cartId and ci.product.id = :productId
        """)
    Optional<CartItemEntity> findByCartAndProduct(@Param("cartId") long cartId,
                                                  @Param("productId") long productId);

    @Query("""
        select coalesce(ci.quantity, 0) from CartItemEntity ci
        where ci.cart.id = :cartId and ci.product.id = :productId
        """)
     Integer findQuantity(@Param("cartId") long cartId,
                         @Param("productId") long productId);

    @Modifying
    @Query("""
        delete from CartItemEntity ci
        where ci.cart.id = :cartId and ci.product.id = :productId
        """)
    int deleteByCartAndProduct(@Param("cartId") long cartId,
                               @Param("productId") long productId);
}