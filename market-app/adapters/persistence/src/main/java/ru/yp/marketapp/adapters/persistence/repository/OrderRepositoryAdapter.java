package ru.yp.marketapp.adapters.persistence.repository;

import org.springframework.data.domain.jaxb.OrderAdapter;
import org.springframework.stereotype.Repository;
import ru.yp.marketapp.adapters.persistence.entity.OrderEntity;
import ru.yp.marketapp.adapters.persistence.jpa.repo.OrderJpaRepository;
import ru.yp.marketapp.appplication.repositories.OrderRepository;
import ru.yp.marketapp.domain.order.Order;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    public OrderRepositoryAdapter(OrderJpaRepository orderJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
    }

    @Override
    public List<Order> findAll() {
        return orderJpaRepository
                .findAllWithItems().stream()
                .map(OrderEntity::toDomain)
                .toList();
    }

    @Override
    public Optional<Order> findById(long id) {
        return orderJpaRepository.findById(id).map(OrderEntity::toDomain);
    }
}
