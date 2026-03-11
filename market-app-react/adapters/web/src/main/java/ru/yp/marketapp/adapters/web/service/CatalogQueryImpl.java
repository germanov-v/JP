package ru.yp.marketapp.adapters.web.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yp.marketapp.adapters.web.service.base.CatalogQuery;
import ru.yp.marketapp.adapters.web.view.ItemView;
import ru.yp.marketapp.appplication.model.SortEnum;
import ru.yp.marketapp.appplication.repositories.ProductRepository;
import ru.yp.marketapp.appplication.result.PageResult;

import java.util.Optional;

@Service
public class CatalogQueryImpl implements CatalogQuery {

    private final ProductRepository productRepository;

    public CatalogQueryImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Mono<PageResult<ItemView>> findItems(String search, SortEnum sort, int pageNumber, int pageSize, long cartId) {
        return productRepository.findItemsCount(search, sort, pageNumber, pageSize,
                        cartId)
                .map(page -> {
                    var items = page.items().stream()
                            .map(item -> ItemView.fromDomainCount(item.product(), item.count()))
                            .toList();

                    return new PageResult<>(
                            items,
                            page.pageNumber(),
                            page.pageSize(),
                            page.hasPrev(),
                            page.hasNext()
                    );
                });
    }

    @Override
    public Mono<ItemView> findItem(long id, Long cartId) {
        return productRepository.findItemById(id, cartId)
                .map(item -> ItemView.fromDomainCount(item.product(), item.count()));
    }
}
