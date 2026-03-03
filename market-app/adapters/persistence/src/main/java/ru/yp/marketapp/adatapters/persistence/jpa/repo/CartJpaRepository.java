package ru.yp.marketapp.adatapters.persistence.jpa.repo;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yp.marketapp.adatapters.persistence.entity.CartEntity;

import java.util.List;
import java.util.UUID;


public interface CartJpaRepository extends JpaRepository<CartEntity, Long> {

    @Query(
           // value = "select c.id from CartEntity c order by c.id desc",
          //  countQuery = "select count(c) from CartEntity c"
            value = "select c.id from market.cart order by c.id desc",
            countQuery = "select count(*) from market.cart",
            nativeQuery = true
    )
    Page<Long> findPageIds(Pageable pageable);


    @Query(value = """

            select distinct c.*
             from market.cart c
             left join market.cart_item i on i.cart_id = c.id
             left join market.product p on p.id = i.product_id
                 where c.id = any(:ids)
             order by c.id desc
           """,
            nativeQuery = true

    )
    List<CartEntity> findByIds(@Param("ids") List<Long> ids);

}
