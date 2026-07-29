package com.pasteleriaPri.Pasteleria.service;

import com.pasteleriaPri.Pasteleria.entity.BoxSize;
import com.pasteleriaPri.Pasteleria.entity.BoxType;
import com.pasteleriaPri.Pasteleria.entity.ProductBox;

import java.util.List;
import java.util.Optional;

public interface IProductBoxService {
    ProductBox save(ProductBox productBox);
    Optional<ProductBox> findById(Long id);
    List<ProductBox> findAll();
    void deleteById(Long id);
    ProductBox update(Long id, ProductBox productBox);
    List<ProductBox> findByBoxType(BoxType boxType);
    List<ProductBox> findByBoxSize(BoxSize boxSize);
}
