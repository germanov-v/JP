package ru.yp.marketapp.adapters.web.view;

public record PagingView (
        int pageNumber,
        int pageSize,
        boolean hasPrevious,
        boolean hasNext
) {}