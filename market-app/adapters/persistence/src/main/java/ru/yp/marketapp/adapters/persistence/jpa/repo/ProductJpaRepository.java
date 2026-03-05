package ru.yp.marketapp.adapters.persistence.jpa.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yp.marketapp.adapters.persistence.entity.ProductEntity;

import java.util.Optional;
import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
    Optional<ProductEntity> findByGuidId(UUID guidId);


    @Query("""
        select p from ProductEntity p
        where (:q = '' or lower(p.title) like lower(concat('%', :q, '%'))
                    or lower(p.description) like lower(concat('%', :q, '%')))
        """)
    Page<ProductEntity> search(@Param("q") String q, Pageable pageable);

    @Query("""
        select p from ProductEntity p
        where p.id = :id
        """)
    Optional<ProductEntity> findOne(@Param("id") long id);
}
