package ru.yp.marketapp.appplication.result;

import java.util.List;

public record PageResult<T>(List<T> items, int pageNumber, int pageSize, boolean hasPrev, boolean hasNext) {}