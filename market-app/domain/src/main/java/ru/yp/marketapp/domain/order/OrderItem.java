package ru.yp.marketapp.domain.order;

import ru.yp.marketapp.domain.base.AggregateRoot;
import ru.yp.marketapp.domain.base.ProductContainer;

public class OrderItem extends ProductContainer {

    private long orderId;


    private Order order;


    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
