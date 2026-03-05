package ru.yp.marketapp.adapters.persistence.jpa.repo;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yp.marketapp.adapters.persistence.entity.OrderEntity;

import java.util.List;
import java.util.Optional;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {

    @EntityGraph(attributePaths = {"items", "items.product"})
    @Query("select o from OrderEntity o")
    List<OrderEntity> findAllWithItems();

    @EntityGraph(attributePaths = {"items", "items.product"})
    Optional<OrderEntity> findById(Long id);
}