package ru.yp.marketapp.adapters.web.service;

import org.springframework.stereotype.Service;
import ru.yp.marketapp.appplication.model.SortEnum;
import ru.yp.marketapp.appplication.repositories.ProductRepository;
import ru.yp.marketapp.appplication.result.PageResult;
import ru.yp.marketapp.adapters.web.service.base.CatalogQuery;
import ru.yp.marketapp.adapters.web.view.ItemView;

import java.util.List;
import java.util.Optional;

@Service
public class CatalogQueryImpl implements CatalogQuery {

    private final ProductRepository productRepository;

    public CatalogQueryImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public PageResult<ItemView> findItems(String search, SortEnum sort, int pageNumber, int pageSize
            , long cartId) {


        var page = productRepository.findItemsCount(search,
                sort, pageNumber, pageSize, Optional.of(cartId));

        var items = page.items().stream()
                .map(item->ItemView.fromDomainCount(item.product(), item.count()))
                .toList();
        return new PageResult<>(items,
                page.pageNumber(),
                page.pageSize(),
                page.hasPrev(),
                page.hasNext());
    }

    @Override
    public Optional<ItemView> findItem(long id) {
        return productRepository.findItemById(id, null)
                .map(item->ItemView.fromDomainCount(item.product(), item.count()));
    }
}
