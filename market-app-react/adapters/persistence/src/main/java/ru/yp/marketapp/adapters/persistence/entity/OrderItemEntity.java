package ru.yp.marketapp.adapters.persistence.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import ru.yp.marketapp.domain.order.OrderItem;

import java.time.OffsetDateTime;
import java.util.UUID;


@Table(name = "order_item", schema = "market")
public class OrderItemEntity {

    @Id
    private Long id;

    @Column("guid_id")
    private UUID guidId;

    @Column("created_at")
    private OffsetDateTime createdAt;

    @Column("order_id")
    private Long orderId;

    @Column("product_id")
    private Long productId;


    @Column("quantity")
    private int quantity;


    public OrderItem toDomain() {
        var result = new OrderItem();
        result.setId(id);
        result.setGuidId(guidId);
        result.setCreatedAt(createdAt);
        result.setOrderId(orderId);
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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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