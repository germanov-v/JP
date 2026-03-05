package ru.yp.marketapp.adapters.web.view;

import ru.yp.marketapp.domain.order.Order;
import ru.yp.marketapp.domain.order.OrderItem;
import ru.yp.marketapp.domain.product.Product;

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
