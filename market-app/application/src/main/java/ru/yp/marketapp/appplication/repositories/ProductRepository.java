package ru.yp.marketapp.appplication.repositories;

import product.Product;
import ru.yp.marketapp.appplication.model.SortEnum;
import ru.yp.marketapp.appplication.result.PageResult;
import ru.yp.marketapp.appplication.result.ProductCountResult;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Optional<Product> findByGuidId(UUID guidId);

    Product save(Product product);

    Optional<Product> findById(Long id);

    PageResult<ProductCountResult> findItemsCount(String search, SortEnum sort, int pageNumber,
                                                  int pageSize,
                                                  Optional<Long> cartId);

    Optional<ProductCountResult> findItemById(long id,Long  cartId);
}
