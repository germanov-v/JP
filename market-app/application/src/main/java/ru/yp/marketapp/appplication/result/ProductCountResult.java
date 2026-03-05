package ru.yp.marketapp.appplication.result;

import ru.yp.marketapp.domain.product.Product;

public record ProductCountResult(Product product, Integer count) {
}
