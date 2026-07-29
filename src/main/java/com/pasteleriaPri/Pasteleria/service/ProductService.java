package com.pasteleriaPri.Pasteleria.service;

import com.pasteleriaPri.Pasteleria.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {

    @Override
    public Product save(Product product) {
        return null;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Product> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Product update(Long id, Product product) {
        return null;
    }

    @Override
    public List<Product> findByProductTypeId(Long productTypeId) {
        return List.of();
    }
}
