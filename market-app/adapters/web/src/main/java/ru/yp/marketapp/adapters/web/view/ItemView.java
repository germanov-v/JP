package ru.yp.marketapp.adapters.web.view;

import product.Product;

import java.math.BigDecimal;

public record ItemView(
        long id,
        String title,
        String description,
        String imgPath,
        BigDecimal price,
        int count
) {
    public static ItemView emptyCell() {
        return new ItemView(-1, "", "", "", BigDecimal.ZERO, 0);
    }

    public static ItemView fromDomainCount(Product product, int count) {

        return new ItemView(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getImgPath(),
                BigDecimal.valueOf(product.getPrice()),
                count
        );
    }
}
