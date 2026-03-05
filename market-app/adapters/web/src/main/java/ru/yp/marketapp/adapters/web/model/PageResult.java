package ru.yp.marketapp.adapters.web.model;

import java.util.List;

public record PageResult<T>(List<T> items, int pageNumber, int pageSize, boolean hasPrev, boolean hasNext) {}