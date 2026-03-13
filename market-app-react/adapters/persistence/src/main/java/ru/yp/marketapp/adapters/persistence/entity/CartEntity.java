package ru.yp.marketapp.adapters.persistence.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import ru.yp.marketapp.domain.cart.Cart;
import ru.yp.marketapp.domain.cart.CartItem;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Table(name = "cart", schema = "market")
public class CartEntity {

    @Id
    private Long id;

    @Column("guid_id")
    private UUID guidId;

    @Column("created_at")
    private OffsetDateTime createdAt;

    public Cart toDomain() {
        var result = new Cart();
        result.setId(id);
        result.setGuidId(guidId);
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



    // TODO: CartItem по cart_id - подгрузим по идее
//    public List<CartItemEntity> getItems() {
//        return items;
//    }
//
//    public void setItems(List<CartItemEntity> items) {
//        this.items = items;
//    }
}
