package ru.yp.marketapp.adapters.persistence.entity;


import jakarta.persistence.*;
import order.OrderItem;
import org.hibernate.annotations.UuidGenerator;
import product.Product;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "order_item", schema = "market",
        indexes = {
                @Index(name = "ix_order_item_order_id", columnList = "order_id"),
                @Index(name = "ix_order_item_product_id", columnList = "product_id"),
                @Index(name = "ix_order_item_guid_id", columnList = "guid_id")
        })
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guid_id", nullable = false, updatable = false, insertable = false)
    @UuidGenerator
    private UUID guidId;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
   private OffsetDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(nullable = false)
    private int quantity;


    public OrderItem toDomain(){
        var result = new OrderItem();
        result.setId(id);
        result.setOrderId(order.getId());
        result.setProductId(product.getId());
        result.setQuantity(quantity);
        result.setCreatedAt(createdAt);
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

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
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