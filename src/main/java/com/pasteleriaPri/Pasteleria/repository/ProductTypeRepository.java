package com.pasteleriaPri.Pasteleria.repository;

import com.pasteleriaPri.Pasteleria.entity.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {
}
