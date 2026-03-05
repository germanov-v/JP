package ru.yp.marketapp.adapters.persistence.entity;

import jakarta.persistence.*;
import ru.yp.marketapp.domain.order.Order;
import ru.yp.marketapp.domain.order.OrderItem;
import org.hibernate.annotations.UuidGenerator;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "\"order\"", schema = "market",
        indexes = @Index(name = "ix_order_guid_id", columnList = "guid_id"))
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "guid_id", nullable = false, updatable = false, insertable = false)
    @UuidGenerator
    private UUID guidId;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> items = new ArrayList<>();


    public Order toDomain(){
        var result = new Order();
        result.setGuidId(guidId);
        result.setCreatedAt(createdAt);
        result.setId(id);

        result.setItems(getItems().stream().map(OrderItemEntity::toDomain).toList());
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

    public List<OrderItemEntity> getItems() {
        return items;
    }

    public void setItems(List<OrderItemEntity> items) {
        this.items = items;
    }
}