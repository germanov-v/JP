package ru.yp.marketapp.domain.base;

import ru.yp.marketapp.domain.product.Product;

public abstract class  ProductContainer  extends AggregateRoot {

    protected Product product;

    protected Long productId;


    protected int quantity;


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
