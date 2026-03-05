package ru.yp.marketapp.domain.order;

import ru.yp.marketapp.domain.base.AggregateRoot;

import java.util.List;

public class Order extends AggregateRoot {

    private List<OrderItem> items;


    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
