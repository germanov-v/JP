package ru.yp.marketapp.adapters.web.view;

import java.math.BigDecimal;

public record OrderLineView(
        long itemId,
        String title,
        BigDecimal price,
        int count
) {}
