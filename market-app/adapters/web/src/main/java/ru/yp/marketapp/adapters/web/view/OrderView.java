package ru.yp.marketapp.adapters.web.view;

import ru.yp.marketapp.domain.order.Order;
import ru.yp.marketapp.domain.order.OrderItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public record OrderView(
        long id,
        List<OrderLineView> items,
        BigDecimal totalSum
) {

    public static OrderView fromDomain(Order entity) {


//        var t = entity.getItems().stream().map(
//                OrderLineView::fromDomain);
        var sum =   entity.getItems().stream().mapToLong(item->
                        item.getProduct().getPrice()
                                *item.getQuantity()
                   ).sum();

        return new OrderView(
                entity.getId(),
                entity.getItems().stream().map(
                        OrderLineView::fromDomain)
                        .toList(),
                BigDecimal.valueOf(  sum)
        );
        //               entity.getItems().stream().map(item->
//                     BigDecimal.valueOf(  item.getProduct().getPrice()
//                               *item.getQuantity())),
    }

}