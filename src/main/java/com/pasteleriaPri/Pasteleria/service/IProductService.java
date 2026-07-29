package com.pasteleriaPri.Pasteleria.service;

import com.pasteleriaPri.Pasteleria.entity.Product;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    Product save(Product product);
    Optional<Product> findById(Long id);
    List<Product> findAll();
    void deleteById(Long id);
    Product update(Long id, Product product);
    List<Product> findByProductTypeId(Long productTypeId);
}
