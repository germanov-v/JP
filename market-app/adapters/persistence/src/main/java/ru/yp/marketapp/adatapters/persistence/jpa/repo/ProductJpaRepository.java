package ru.yp.marketapp.adatapters.persistence.jpa.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yp.marketapp.adatapters.persistence.entity.ProductEntity;

import java.util.Optional;
import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByGuidId(UUID guidId);

}
