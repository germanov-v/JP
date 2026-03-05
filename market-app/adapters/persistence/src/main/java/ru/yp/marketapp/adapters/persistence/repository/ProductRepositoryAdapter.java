package ru.yp.marketapp.adapters.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import product.Product;
import ru.yp.marketapp.adapters.persistence.entity.ProductEntity;
import ru.yp.marketapp.adapters.persistence.jpa.repo.ProductJpaRepository;
import ru.yp.marketapp.appplication.model.SortEnum;
import ru.yp.marketapp.appplication.repositories.ProductRepository;
import ru.yp.marketapp.appplication.result.PageResult;

import java.util.Optional;
import java.util.UUID;



@Repository
public class ProductRepositoryAdapter implements ProductRepository {

    private ProductJpaRepository productJpaRepository;

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

    @Override
    public PageResult<Product> findItems(String search, SortEnum sort, int pageNumber, int pageSize) {
        Sort springSort = switch (sort) {
            case NO -> Sort.unsorted();
            case ALPHA -> Sort.by("title").ascending();
            case PRICE -> Sort.by("price").ascending();
        };

        //pageNumber  1,  Pageable  0
        int pageIndex = Math.max(0, pageNumber - 1);
        Pageable pageable = PageRequest.of(pageIndex, pageSize, springSort);

        Page<?> page = productJpaRepository.search(search == null ? "" : search.trim(), pageable);

        var items = page.getContent().stream()
                .map(p -> {
                    var product = (ProductEntity) p;
                    // todo: по идее с корзины надо брать
                    //int count =
                    return product.toDomain();
                })
                .toList();

        return new PageResult<>(
                items,
                pageNumber,
                pageSize,
                page.hasPrevious(),
                page.hasNext()
        );
    }
}
