package ru.yp.marketapp.adapters.web.view;

import order.Order;
import order.OrderItem;
import product.Product;

import java.math.BigDecimal;

public record OrderLineView(
        long itemId,
       // String title,
        BigDecimal price,
        int count
) {
    public static OrderLineView fromDomain(OrderItem entity) {

        return new OrderLineView(
                entity.getId(),
                BigDecimal.valueOf(entity.getQuantity()*entity.getProduct().getPrice()),
                        entity.getQuantity()  );
        //               entity.getItems().stream().map(item->
//                     BigDecimal.valueOf(  item.getProduct().getPrice()
//                               *item.getQuantity())),
    }


}
