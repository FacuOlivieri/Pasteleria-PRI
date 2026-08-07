package com.pasteleriaPri.Pasteleria.repository;

import com.pasteleriaPri.Pasteleria.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByProductType_IdProductType(Long productTypeId);
    List<Product> findByProdPriceBetweenOrderByProdPriceAsc(Double minPrice, Double maxPrice);

    /**
     * Catalog search where every criterion is optional: a null parameter simply
     * drops its condition. The join is a LEFT JOIN on purpose — the implicit
     * inner join of "p.productType.idProductType" would hide every product that
     * has no category, even when no category filter was requested.
     */
    @Query("""
            SELECT p FROM Product p
            LEFT JOIN p.productType pt
            WHERE (:typeId IS NULL OR pt.idProductType = :typeId)
              AND (:minPrice IS NULL OR p.prodPrice >= :minPrice)
              AND (:maxPrice IS NULL OR p.prodPrice <= :maxPrice)
            ORDER BY p.prodPrice ASC
            """)
    List<Product> findAllByFilters(@Param("typeId") Long typeId,
                                   @Param("minPrice") Double minPrice,
                                   @Param("maxPrice") Double maxPrice);
}
