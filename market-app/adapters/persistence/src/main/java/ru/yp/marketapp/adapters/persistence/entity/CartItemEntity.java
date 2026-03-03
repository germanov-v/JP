package ru.yp.marketapp.adapters.persistence.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "cart_item", schema = "market",
        uniqueConstraints = @UniqueConstraint(name = "uq_cart_item_cart_product", columnNames = {"cart_id", "product_id"}),
        indexes = {
                @Index(name = "ix_cart_item_cart_id", columnList = "cart_id"),
                @Index(name = "ix_cart_item_product_id", columnList = "product_id"),
                @Index(name = "ix_cart_item_guid_id", columnList = "guid_id")
        })
public class CartItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guid_id", nullable = false, updatable = false, insertable = false)
    @UuidGenerator
    private UUID guidId;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
     private OffsetDateTime createdAt;

    // todo: test FetchType.EAGER
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    private CartEntity cart;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(nullable = false)
    private int quantity;

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

    public CartEntity getCart() {
        return cart;
    }

    public void setCart(CartEntity cart) {
        this.cart = cart;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}