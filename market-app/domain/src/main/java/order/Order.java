package order;

import base.AggregateRoot;

import java.util.List;

public class Order extends AggregateRoot {

    private List<OrderItem> items;

}
