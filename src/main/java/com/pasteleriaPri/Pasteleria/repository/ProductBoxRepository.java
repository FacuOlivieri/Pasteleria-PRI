package com.pasteleriaPri.Pasteleria.repository;

import com.pasteleriaPri.Pasteleria.entity.BoxSize;
import com.pasteleriaPri.Pasteleria.entity.BoxType;
import com.pasteleriaPri.Pasteleria.entity.ProductBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductBoxRepository extends JpaRepository<ProductBox, Long> {
    List<ProductBox> findByBoxType(BoxType boxType);
    List<ProductBox> findByBoxSize(BoxSize boxSize);
}
