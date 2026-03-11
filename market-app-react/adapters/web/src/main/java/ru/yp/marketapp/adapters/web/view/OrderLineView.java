package ru.yp.marketapp.adapters.web.view;

import ru.yp.marketapp.domain.order.OrderItem;

import java.math.BigDecimal;

public record OrderLineView(
        long itemId,
        String title,
        BigDecimal price,
        int count
) {
    public static OrderLineView fromDomain(OrderItem entity) {

        long price =0;
        if(entity.getProduct()!=null&&entity.getProduct().getPrice()!=null) {
            price = entity.getProduct().getPrice();
        }

        return new OrderLineView(
                entity.getId(),
                entity.getProduct().getTitle(),
                BigDecimal.valueOf(entity.getQuantity()*price),
                        entity.getQuantity()
           );
        //               entity.getItems().stream().map(item->
//                     BigDecimal.valueOf(  item.getProduct().getPrice()
//                               *item.getQuantity())),
    }


}
