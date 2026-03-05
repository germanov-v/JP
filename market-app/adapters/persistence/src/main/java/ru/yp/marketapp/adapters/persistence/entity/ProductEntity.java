package ru.yp.marketapp.adapters.persistence.entity;


import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import ru.yp.marketapp.domain.product.Product;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "product", schema = "market",
   indexes = @Index(name="ix_product_guid_id", columnList = "guid_id"))
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name="guid_id", nullable = false, updatable = false, insertable = true)
    @UuidGenerator
    private UUID guidId;

    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private OffsetDateTime createdAt;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column(name = "img_path")
    private String imgPath;

    @Column(nullable = false)
    private long price;

    @Column(nullable = false)
    private int count;



    public Product toDomain(){
        var result = new Product();
        result.setGuidId(guidId);
        result.setTitle(title);
        result.setDescription(description);
        result.setImgPath(imgPath);
        result.setPrice(price);
        result.setCount(count);
        result.setId(id);
        return result;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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
