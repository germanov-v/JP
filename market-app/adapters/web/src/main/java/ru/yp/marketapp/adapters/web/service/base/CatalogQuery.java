package ru.yp.marketapp.adapters.web.service.base;

import ru.yp.marketapp.appplication.result.PageResult;
import ru.yp.marketapp.adapters.web.view.ItemView;
import ru.yp.marketapp.adapters.web.view.SortView;

import java.util.Optional;

public interface CatalogQuery {
    PageResult<ItemView> findItems(String search, SortView sort, int pageNumber, int pageSize);
    Optional<ItemView> findItem(long id);
}
