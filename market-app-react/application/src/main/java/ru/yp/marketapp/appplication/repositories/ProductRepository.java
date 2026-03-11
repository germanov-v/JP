package ru.yp.marketapp.appplication.repositories;

import reactor.core.publisher.Mono;
import ru.yp.marketapp.appplication.model.SortEnum;
import ru.yp.marketapp.appplication.result.PageResult;
import ru.yp.marketapp.appplication.result.ProductCountResult;
import ru.yp.marketapp.domain.product.Product;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {
    Mono<Product> findByGuidId(UUID guidId);

    Mono<Product> save(Product product);

    Mono<Product> findById(Long id);

    Mono<PageResult<ProductCountResult>> findItemsCount(String search, SortEnum sort, int pageNumber,
                                                  int pageSize,
                                                  Long cartId);

    Mono<ProductCountResult> findItemById(long id,Long  cartId);


}
