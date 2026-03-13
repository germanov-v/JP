package ru.yp.marketapp.adapters.persistence.repository;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.yp.marketapp.appplication.repositories.OrderRepository;
import ru.yp.marketapp.domain.order.Order;
import ru.yp.marketapp.domain.order.OrderItem;
import ru.yp.marketapp.domain.product.Product;

import java.util.ArrayList;
import java.util.Map;


@Repository
public class OrderRepositoryAdapter implements OrderRepository {

    // TODO: using DatabaseClient
    private final DatabaseClient databaseClient;

    public OrderRepositoryAdapter(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }


    @Override
    public Flux<Order> findAll() {
        var sql = """
                select o.id as order_id,
                       oi.id as item_id,
                       oi.quantity as item_quantity,
                       p.id as product_id,
                       p.title as product_title,
                       p.description as product_description,
                       p.price as product_price
                from market."order" o
                left join market.order_item oi on oi.order_id = o.id
                left join market.product p on p.id = oi.product_id
                order by o.id, oi.id
                """;

        return databaseClient.sql(sql)
                .fetch()
                .all() // all
                //.bufffer
                .bufferUntilChanged(r -> r.get("order_id"))
                .map(this::toOrder);
    }

    @Override
    public Mono<Order> findById(long id) {
        var sql = """
                select o.id as order_id,
                       oi.id as item_id,
                       oi.quantity as item_quantity,
                       p.id as product_id,
                       p.title as product_title,
                       p.description as product_description,
                       p.price as product_price
                from market."order" o
                left join market.order_item oi on oi.order_id = o.id
                left join market.product p on p.id = oi.product_id
                where o.id = :orderId
                order by o.id, oi.id
                """;

        var result = databaseClient.sql(sql)
                .bind("orderId", id)
                .fetch()
                .all()
                .collectList()// все в память - реактивщина не поможет, но речь про один элемент поэтому норм
                .filter(rows -> !rows.isEmpty())
                .map(this::toOrder);


        return result;
    }

    private Order toOrder(java.util.List<Map<String, Object>> rows) {
        Map<String, Object> first = rows.get(0);

        Order order = new Order();
        order.setId(((Number) first.get("order_id")).longValue());
        var items = new ArrayList<OrderItem>(rows.size());
        for (Map<String, Object> row : rows) {
            if (row.get("item_id") == null) {
                continue;
            }

            OrderItem item = new OrderItem();
            item.setId(((Number) row.get("item_id")).longValue());
            //    item.setQuantity(((Long) row.get("item_quantity")).intValue());

            item.setQuantity(((Number) row.get("item_quantity")).intValue());

            var prodct = new Product();
            prodct.setId(((Number) row.get("product_id")).longValue());
            prodct.setPrice(((Number) row.get("product_price")).longValue());
            prodct.setTitle((String) row.get("product_title"));
            prodct.setDescription((String) row.get("product_description"));

            item.setProduct(prodct);
            items.add(item);

        }
        order.setItems(items);
        return order;
    }
}
