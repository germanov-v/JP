package ru.yp.marketapp.adatapters.persistence.repository;

import org.springframework.stereotype.Repository;
import product.Product;
import ru.yp.marketapp.appplication.repositories.ProductRepository;

import java.util.Optional;
import java.util.UUID;



@Repository
public class ProductRepositryAdapter implements ProductRepository {
    @Override
    public Optional<Product> findByGuidId(UUID guidId) {
        return Optional.empty();
    }

    @Override
    public Product save(Product product) {
        return null;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.empty();
    }
}
