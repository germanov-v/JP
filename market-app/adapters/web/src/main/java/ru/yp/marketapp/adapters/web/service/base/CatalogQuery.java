package ru.yp.marketapp.adapters.web.service.base;

import ru.yp.marketapp.appplication.model.SortEnum;
import ru.yp.marketapp.appplication.result.PageResult;
import ru.yp.marketapp.adapters.web.view.ItemView;

import java.util.Optional;

public interface CatalogQuery {
    PageResult<ItemView> findItems(String search, SortEnum sort, int pageNumber, int pageSize
    , long cartId);
    Optional<ItemView> findItem(long id);
}
