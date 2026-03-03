package ru.yp.marketapp.appplication.repositories;

import product.Product;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Optional<Product> findByGuidId(UUID guidId);

    Product save(Product product);

    Optional<Product> findById(Long id);
}
