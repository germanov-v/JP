package ru.yp.marketapp.adapters.web.service.base;

import reactor.core.publisher.Mono;
import ru.yp.marketapp.adapters.web.view.ItemView;
import ru.yp.marketapp.appplication.model.SortEnum;
import ru.yp.marketapp.appplication.result.PageResult;

import java.util.Optional;

public interface CatalogQuery {
    Mono<PageResult<ItemView>> findItems(String search, SortEnum sort, int pageNumber, int pageSize, long cartId);

    Mono<ItemView> findItem(long id, Long cartId);
}
