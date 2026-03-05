package ru.yp.marketapp.adapters.web.view;

import java.math.BigDecimal;
import java.util.List;

public record OrderView(
        long id,
        List<OrderLineView> items,
        BigDecimal totalSum
) {}