package ru.yp.marketapp.adapters.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.yp.marketapp.domain.product.Product;
import ru.yp.marketapp.adapters.persistence.entity.CartEntity;
import ru.yp.marketapp.adapters.persistence.entity.ProductEntity;
import ru.yp.marketapp.adapters.persistence.jpa.repo.CartItemJpaRepository;
import ru.yp.marketapp.adapters.persistence.jpa.repo.CartJpaRepository;
import ru.yp.marketapp.adapters.persistence.jpa.repo.ProductJpaRepository;
import ru.yp.marketapp.appplication.model.SortEnum;
import ru.yp.marketapp.appplication.repositories.ProductRepository;
import ru.yp.marketapp.appplication.result.PageResult;
import ru.yp.marketapp.appplication.result.ProductCountResult;

import java.util.Optional;
import java.util.UUID;



@Repository
public class ProductRepositoryAdapter implements ProductRepository {

    private ProductJpaRepository productJpaRepository;

    private CartItemJpaRepository cartItemJpaRepository;

    public ProductRepositoryAdapter(ProductJpaRepository productJpaRepository, CartItemJpaRepository cartItemJpaRepository) {
        this.productJpaRepository = productJpaRepository;
        this.cartItemJpaRepository = cartItemJpaRepository;
    }

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
    public PageResult<ProductCountResult> findItemsCount(String search, SortEnum sort, int pageNumber, int pageSize,
                                                         Optional<Long> cartId) {
        Sort springSort = switch (sort) {
            case NO -> Sort.unsorted();
            case ALPHA -> Sort.by("title").ascending();
            case PRICE -> Sort.by("price").ascending();
        };

        //pageNumber  1,  Pageable  0
        int pageIndex = Math.max(0, pageNumber - 1);
        Pageable pageable = PageRequest.of(pageIndex, pageSize, springSort);

        Page<ProductEntity> page = productJpaRepository.search(search == null ? "" : search.trim(), pageable);

        var items = page.getContent().stream()
                .map(p -> {
                    // todo: по идее с корзины надо брать
                    int count = 0;
                    if(cartId.isPresent()) {
                        count = cartItemJpaRepository.findQuantity(cartId.get(), p.getId()).orElse(0);
                    }
                    return new ProductCountResult(p.toDomain(), count);
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

    // Optional не Optional cartId
    public Optional<ProductCountResult> findItemById(long id, Long cartId) {
        var productEntity = productJpaRepository.findById(id);
        if(productEntity.isEmpty()) {
              return Optional.empty();
        }
        var count = 0;
        if (cartId != null) {
           count = cartItemJpaRepository.findQuantity(cartId, id).orElse(0);
        }


       return Optional.of(new ProductCountResult(productEntity.get().toDomain(), count));



    }
}
