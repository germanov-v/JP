package ru.yp.marketapp.adapters.web.view;

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
}
