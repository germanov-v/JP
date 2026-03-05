package ru.yp.marketapp.adapters.persistence.repository;

import ru.yp.marketapp.adapters.persistence.entity.CartEntity;
import ru.yp.marketapp.adapters.persistence.jpa.repo.CartJpaRepository;
import ru.yp.marketapp.appplication.repositories.CartRepository;

import java.util.Optional;

public class CartRepositoryAdapter implements CartRepository {

    private final CartJpaRepository cartJpaRepository;

    public CartRepositoryAdapter(CartJpaRepository cartJpaRepository) {
        this.cartJpaRepository = cartJpaRepository;
    }

    @Override
    public void changeCount(long cartId, long productId, long count) {

    }

    public CartEntity createEmptyCart(long cartId) {
        var entity = new CartEntity();
        return cartJpaRepository.save(entity);
    }

    public Optional<CartEntity> getCartById(long cartId) {
        return cartJpaRepository.findById(cartId);
    }
}
