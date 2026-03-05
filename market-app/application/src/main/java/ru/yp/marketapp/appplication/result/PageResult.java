package ru.yp.marketapp.appplication.result;

import java.util.List;

public record PageResult<T>(List<T> items, int pageNumber, int pageSize, boolean hasPrev, boolean hasNext) {
    public static <T> PageResult<T> empty(int pageNumber, int pageSize) {
        return new PageResult<>(List.of(), pageNumber, pageSize, false, false);
    }
}