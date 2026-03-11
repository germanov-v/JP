package ru.yp.marketapp.adapters.persistence.repository;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;
import ru.yp.marketapp.appplication.model.CartActionEnum;
import ru.yp.marketapp.appplication.repositories.CartRepository;

@Repository
public class CartRepositoryAdapter implements CartRepository {

    private final DatabaseClient databaseClient;
    private final TransactionalOperator transactionalOperator;

    public CartRepositoryAdapter(DatabaseClient databaseClient, TransactionalOperator transactionalOperator) {
        this.databaseClient = databaseClient;
        this.transactionalOperator = transactionalOperator;
    }

    @Override
    public Mono<Boolean> existsById(long cartId) {
        return databaseClient.sql("""
                select exists(select 1 from market.cart where id = :id)
                """)
                .bind("id", cartId)
                .map((row, meta) -> Boolean.TRUE.equals(row.get(0, Boolean.class)))
                .one();
    }

    @Override
    public Mono<Void> changeCount(long cartId, long productId, CartActionEnum action) {
        Mono<Void> logic = ensureCartExists(cartId)
                .then(findQuantity(cartId, productId))
                .flatMap(quantity -> {
                    if (action == CartActionEnum.PLUS) {
                        if (quantity == 0) {
                            return insertCartItem(cartId, productId, 1);
                        }
                        return updateQuantity(cartId, productId, quantity + 1);
                    }

                    if (quantity == 0) {
                        return Mono.empty();
                    }

                    if (quantity == 1) {
                        return deleteCartItem(cartId, productId);
                    }

                    return updateQuantity(cartId, productId, quantity - 1);
                });

        return transactionalOperator.transactional(logic);
    }

    @Override
    public Mono<Long> getOrCreateCartId() {
        return databaseClient.sql("""
                insert into market.cart default values
                returning id
                """)
                .map((row, meta) -> row.get("id", Long.class))
                .one();
    }


    // TODO: попробовать default values - в запросе
    @Override
    public Mono<Long> createEmptyCart() {
        return databaseClient.sql("""
            insert into market.cart default values
            RETURNING id
            """)
                .map((row, meta) -> row.get("id", Long.class))
                .one();
    }

    @Override
    public Mono<Integer> getQuantity(long cartId, long productId) {
        return findQuantity(cartId, productId);
    }

    private Mono<Void> ensureCartExists(long cartId) {
        return existsById(cartId)
                .flatMap(exists -> exists
                        ? Mono.empty()
                        : Mono.error(new IllegalArgumentException("Cart not found: " + cartId)));
    }

    private Mono<Integer> findQuantity(long cartId, long productId) {
        return databaseClient.sql("""
                select quantity
                from market.cart_item
                where cart_id = :cartId and product_id = :productId
                """)
                .bind("cartId", cartId)
                .bind("productId", productId)
                .map((row, meta) -> row.get("quantity", Integer.class))
                .one()
                .defaultIfEmpty(0);
    }

    private Mono<Void> insertCartItem(long cartId, long productId, int quantity) {
        return databaseClient.sql("""
                insert into market.cart_item(cart_id, product_id, quantity)
                values (:cartId, :productId, :quantity)
                """)
                .bind("cartId", cartId)
                .bind("productId", productId)
                .bind("quantity", quantity)
                .then();
    }

    private Mono<Void> updateQuantity(long cartId, long productId, int quantity) {
        return databaseClient.sql("""
                update market.cart_item
                   set quantity = :quantity
                 where cart_id = :cartId and product_id = :productId
                """)
                .bind("quantity", quantity)
                .bind("cartId", cartId)
                .bind("productId", productId)
                .then();
    }

    private Mono<Void> deleteCartItem(long cartId, long productId) {
        return databaseClient.sql("""
                delete from market.cart_item
                where cart_id = :cartId and product_id = :productId
                """)
                .bind("cartId", cartId)
                .bind("productId", productId)
                .then();
    }

    public Mono<Void> deleteAllCartItems() {
        return databaseClient.sql("delete from market.cart_item").then();
    }

    public Mono<Void> deleteAllCarts() {
        return databaseClient.sql("delete from market.cart").then();
    }
}