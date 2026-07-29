package com.pasteleriaPri.Pasteleria.service.impl;

import com.pasteleriaPri.Pasteleria.entity.ProductType;
import com.pasteleriaPri.Pasteleria.repository.ProductTypeRepository;
import com.pasteleriaPri.Pasteleria.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductTypeServiceImpl implements ProductTypeService {

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Override
    public ProductType save(ProductType productType) {
        return productTypeRepository.save(productType);
    }

    @Override
    public Optional<ProductType> findById(Long id) {
        return productTypeRepository.findById(id);
    }

    @Override
    public List<ProductType> findAll() {
        return productTypeRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        productTypeRepository.deleteById(id);
    }

    @Override
    public ProductType update(Long id, ProductType productType) {
        productType.setIdProductType(id);
        return productTypeRepository.save(productType);
    }
}
