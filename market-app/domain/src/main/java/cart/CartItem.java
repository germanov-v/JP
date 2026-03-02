package cart;

import base.AggregateRoot;
import base.ProductContainer;
import product.Product;

public class CartItem extends ProductContainer {

    private Long id;

    private Cart cart;

    private Long cartId;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }


}
