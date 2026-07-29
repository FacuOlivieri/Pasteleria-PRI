package com.pasteleriaPri.Pasteleria.service;

import com.pasteleriaPri.Pasteleria.entity.ProductType;

import java.util.List;
import java.util.Optional;

public interface IProductTypeService {
    ProductType save(ProductType productType);
    Optional<ProductType> findById(Long id);
    List<ProductType> findAll();
    void deleteById(Long id);
    ProductType update(Long id, ProductType productType);
}
