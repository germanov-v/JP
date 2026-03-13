package ru.yp.marketapp.adapters.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import ru.yp.marketapp.domain.cart.CartItem;

import java.time.OffsetDateTime;
import java.util.UUID;


@Table(name="cart_item", schema="market")
public class CartItemEntity {

    @Id
    private Long id;

    @Column("guid_id")
    private UUID guidId;

    @Column("created_at")
    private OffsetDateTime createdAt;

    @Column("cart_id")
    private Long cartId;

    @Column("product_id")
    private Long productId;

    private int quantity;

    public CartItem toDomain() {
        var result = new CartItem();
        result.setId(id);
        result.setGuidId(guidId);
        result.setCreatedAt(createdAt);
        result.setCartId(cartId);
        result.setProductId(productId);
        result.setQuantity(quantity);
        return result;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getGuidId() {
        return guidId;
    }

    public void setGuidId(UUID guidId) {
        this.guidId = guidId;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}