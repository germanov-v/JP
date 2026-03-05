package order;

import base.AggregateRoot;

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
