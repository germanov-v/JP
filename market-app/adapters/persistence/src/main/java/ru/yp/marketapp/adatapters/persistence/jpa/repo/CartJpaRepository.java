package ru.yp.marketapp.adatapters.persistence.jpa.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yp.marketapp.adatapters.persistence.entity.CartEntity;

public interface CartJpaRepository extends JpaRepository<CartEntity, Long> {


    //  @Query

}
