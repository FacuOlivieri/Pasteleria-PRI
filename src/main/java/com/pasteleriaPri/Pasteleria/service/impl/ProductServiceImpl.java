package com.pasteleriaPri.Pasteleria.service.impl;

import com.pasteleriaPri.Pasteleria.entity.Product;
import com.pasteleriaPri.Pasteleria.repository.ProductRepository;
import com.pasteleriaPri.Pasteleria.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product update(Long id, Product product) {
        product.setIdProduct(id);
        return productRepository.save(product);
    }

    @Override
    public List<Product> findByProductTypeId(Long productTypeId) {
        return productRepository.findByProductTypeIdProductType(productTypeId);
    }
}
