package ru.yp.marketapp.adapters.web.service;

import org.springframework.stereotype.Service;
import ru.yp.marketapp.appplication.model.SortEnum;
import ru.yp.marketapp.appplication.result.PageResult;
import ru.yp.marketapp.adapters.web.service.base.CatalogQuery;
import ru.yp.marketapp.adapters.web.view.ItemView;

import java.util.List;
import java.util.Optional;

@Service
public class CatalogQueryImpl implements CatalogQuery {
    @Override
    public PageResult<ItemView> findItems(String search, SortEnum sort, int pageNumber, int pageSize) {
        return new PageResult<>(List.of(), pageNumber, pageSize, false, false);
    }

    @Override
    public Optional<ItemView> findItem(long id) {
        return Optional.empty();
    }
}
