package ru.yp.marketapp.adapters.persistence.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.yp.marketapp.adapters.persistence.entity.CartEntity;
import ru.yp.marketapp.adapters.persistence.entity.CartItemEntity;
import ru.yp.marketapp.adapters.persistence.jpa.repo.CartItemJpaRepository;
import ru.yp.marketapp.adapters.persistence.jpa.repo.CartJpaRepository;
import ru.yp.marketapp.adapters.persistence.jpa.repo.ProductJpaRepository;
import ru.yp.marketapp.appplication.model.CartActionEnum;
import ru.yp.marketapp.appplication.repositories.CartRepository;

import java.util.Optional;


@Repository
public class CartRepositoryAdapter implements CartRepository {

    private final CartJpaRepository cartJpaRepository;

    private final CartItemJpaRepository cartItemJpaRepository;

    private final ProductJpaRepository productJpaRepository;

    public CartRepositoryAdapter(CartJpaRepository cartJpaRepository, CartItemJpaRepository cartItemJpaRepository, ProductJpaRepository productJpaRepository) {
        this.cartJpaRepository = cartJpaRepository;
        this.cartItemJpaRepository = cartItemJpaRepository;
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public boolean existsById(long cartId) {
        return cartJpaRepository.existsById(cartId);
    }



    @Override
    @Transactional
    public void changeCount(long cartId, long productId, CartActionEnum action) {
        var   cartEntity =  getCartById(cartId).orElseThrow();

        var item = cartItemJpaRepository.findByCartAndProduct(cartId, productId);

        if (action == CartActionEnum.PLUS){
           if(item.isEmpty()){
               var tempCartItem = new CartItemEntity();

               var product = productJpaRepository.findById(productId).orElseThrow();
               tempCartItem.setProduct(product);
               tempCartItem.setCart(cartEntity);
               tempCartItem.setQuantity(1);
                cartItemJpaRepository.save(tempCartItem);
           }else{
               item.orElseThrow().setQuantity(item.get().getQuantity()+1);
           }
          return;
        }

        if (item.isEmpty()){
            return;
        }

       var cartItem = item.orElseThrow();
        if(cartItem.getQuantity()==1){
            cartItemJpaRepository.delete(cartItem);
        }else {
            cartItem.setQuantity(cartItem.getQuantity()-1);
            cartItemJpaRepository.save(cartItem);
        }
    }

    @Override
    public long getOrCreateCartId() {
        return 0;
    }

    @Override
    public int getQuantity(long cartId, long productId) {
        return cartItemJpaRepository.findByCartAndProduct(cartId, productId)
                //.orElseThrow().getQuantity();
                .map(CartItemEntity::getQuantity)
                .orElse(0);
    }

    public long createEmptyCart() {
        var entity = new CartEntity();
        return createEmptyCartGetEntity().getId();
    }

    public CartEntity createEmptyCartGetEntity() {
        var entity = new CartEntity();
        return cartJpaRepository.save(entity);
    }

    public Optional<CartEntity> getCartById(long cartId) {
        return cartJpaRepository.findById(cartId);
    }




}
