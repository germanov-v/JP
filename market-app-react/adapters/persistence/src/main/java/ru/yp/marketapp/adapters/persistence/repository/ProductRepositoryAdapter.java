package ru.yp.marketapp.adapters.persistence.repository;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import ru.yp.marketapp.appplication.model.SortEnum;
import ru.yp.marketapp.appplication.repositories.ProductRepository;
import ru.yp.marketapp.appplication.result.PageResult;
import ru.yp.marketapp.appplication.result.ProductCountResult;
import ru.yp.marketapp.domain.product.Product;

import java.util.UUID;

@Repository
public class ProductRepositoryAdapter implements ProductRepository {

    private final DatabaseClient databaseClient;

    public ProductRepositoryAdapter(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Mono<Product> findByGuidId(UUID guidId) {
        return databaseClient.sql("""
                select id, guid_id, title, description, price
                from market.product
                where guid_id = :guidId
                """)
                .bind("guidId", guidId)
                .map((row, meta) -> mapProduct(row))
                .one();
    }

    @Override
    public Mono<Product> save(Product product) {
        if (product.getId() == null) {
            return databaseClient.sql("""
                    insert into market.product(guid_id, title, description, price)
                    values (:guidId, :title, :description, :price)
                    returning id, guid_id, title, description, price
                    """)
                    .bind("guidId", product.getGuidId())
                    .bind("title", product.getTitle())
                    .bind("description", product.getDescription())
                    .bind("price", product.getPrice())
                    .map((row, meta) -> mapProduct(row))
                    .one();
        }

        return databaseClient.sql("""
                update market.product
                   set title = :title,
                       description = :description,
                       price = :price
                 where id = :id
                returning id, guid_id, title, description, price
                """)
                .bind("id", product.getId())
                .bind("title", product.getTitle())
                .bind("description", product.getDescription())
                .bind("price", product.getPrice())
                .map((row, meta) -> mapProduct(row))
                .one();
    }

    @Override
    public Mono<Product> findById(Long id) {
        return databaseClient.sql("""
                select id, guid_id, title, description, price
                from market.product
                where id = :id
                """)
                .bind("id", id)
                .map((row, meta) -> mapProduct(row))
                .one();
    }

    public Mono<PageResult<ProductCountResult>> findItemsCount(String search,
                                                               SortEnum sort,
                                                               int pageNumber,
                                                               int pageSize,
                                                               Long cartId) {
        String orderBy = switch (sort) {
            case ALPHA -> "title asc";
            case PRICE -> "price asc";
            case NO -> "id asc";
        };

        int offset = Math.max(0, pageNumber - 1) * pageSize;
        String q = search == null ? "" : search.trim();

        Mono<Long> totalMono = databaseClient.sql("""
                select count(*)
                from market.product
                where (:q = '' or lower(title) like lower('%' || :q || '%')
                               or lower(description) like lower('%' || :q || '%'))
                """)
                .bind("q", q)
                .map((row, meta) -> ((Number) row.get(0)).longValue())
                .one();

        Mono<java.util.List<ProductCountResult>> itemsMono =
                databaseClient.sql("""
        select p.id, p.guid_id, p.title, p.description, p.price,
               coalesce(ci.quantity, 0) as quantity
        from market.product p
        left join market.cart_item ci
          on ci.product_id = p.id
         and ci.cart_id = :cartId
        where (:q = '' or lower(p.title) like lower('%' || :q || '%')
                       or lower(p.description) like lower('%' || :q || '%'))
        order by %s
        limit :limit offset :offset
        """.formatted(orderBy))
                        .bind("q", q)
                        .bind("cartId", cartId==null?"null":cartId)
                        .bind("limit", pageSize)
                        .bind("offset", offset)
                        .map((row, meta) -> new ProductCountResult(
                                mapProduct(row),
                                ((Number) row.get("quantity")).intValue()
                        ))
                        .all()
                        .collectList();

        return Mono.zip(totalMono, itemsMono)
                .map(tuple -> {
                    long total = tuple.getT1();
                    var items = tuple.getT2();
                    boolean hasPrevious = pageNumber > 1;
                    boolean hasNext = (long) pageNumber * pageSize < total;

                    return new PageResult<>(
                            items,
                            pageNumber,
                            pageSize,
                            hasPrevious,
                            hasNext
                    );
                });
    }

    @Override
    public Mono<ProductCountResult> findItemById(long id, Long cartId) {
        String sql = """
            select p.id,
                   p.guid_id,
                   p.title,
                   p.description,
                   p.price,
                   coalesce(ci.quantity, 0) as quantity
            from market.product p
            left join market.cart_item ci
                   on ci.product_id = p.id
                  and ci.cart_id = :cartId
            where p.id = :id
            """;

        var executeSpec = databaseClient.sql(sql)
                .bind("id", id);

        executeSpec = cartId == null
                ? executeSpec.bindNull("cartId", Long.class)
                : executeSpec.bind("cartId", cartId);

        return executeSpec
                .map((row, meta) -> new ProductCountResult(
                        mapProduct(row),
                        row.get("quantity", Integer.class)
                ))
                .one();
    }

    private Product mapProduct(io.r2dbc.spi.Row row) {
        Product product = new Product();
        product.setId(row.get("id", Long.class));
        product.setGuidId(row.get("guid_id", UUID.class));
        product.setTitle(row.get("title", String.class));
        product.setDescription(row.get("description", String.class));
        product.setPrice(row.get("price", Long.class));
        return product;
    }
}