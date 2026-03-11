package ru.yp.marketapp.adapters.persistence.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import ru.yp.marketapp.domain.product.Product;

import java.time.OffsetDateTime;
import java.util.UUID;


@Table(name = "product", schema = "market")
public class ProductEntity {
    @Id
    private Long id;

    @Column("guid_id")
    private UUID guidId;

    @Column("created_at")
    private OffsetDateTime createdAt;

    private String title;
    private String description;

    @Column("img_path")
    private String imgPath;

    private long price;
    private int count;

    public Product toDomain() {
        var result = new Product();
        result.setId(id);
        result.setGuidId(guidId);
        result.setTitle(title);
        result.setDescription(description);
        result.setImgPath(imgPath);
        result.setPrice(price);
        result.setCount(count);
        return result;
    }

    public static ProductEntity fromDomain(Product product) {
        var entity = new ProductEntity();
        entity.setId(product.getId());
        entity.setGuidId(product.getGuidId());
        entity.setTitle(product.getTitle());
        entity.setDescription(product.getDescription());
        entity.setImgPath(product.getImgPath());
        entity.setPrice(product.getPrice());
        entity.setCount(product.getCount());
        return entity;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
