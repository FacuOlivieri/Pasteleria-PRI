package com.pasteleriaPri.Pasteleria.service.impl;

import com.pasteleriaPri.Pasteleria.entity.BoxSize;
import com.pasteleriaPri.Pasteleria.entity.BoxType;
import com.pasteleriaPri.Pasteleria.entity.ProductBox;
import com.pasteleriaPri.Pasteleria.repository.ProductBoxRepository;
import com.pasteleriaPri.Pasteleria.service.ProductBoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductBoxServiceImpl implements ProductBoxService {

    @Autowired
    private ProductBoxRepository productBoxRepository;

    @Override
    public ProductBox save(ProductBox productBox) {
        return productBoxRepository.save(productBox);
    }

    @Override
    public Optional<ProductBox> findById(Long id) {
        return productBoxRepository.findById(id);
    }

    @Override
    public List<ProductBox> findAll() {
        return productBoxRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        productBoxRepository.deleteById(id);
    }

    @Override
    public ProductBox update(Long id, ProductBox productBox) {
        productBox.setIdProductBox(id);
        return productBoxRepository.save(productBox);
    }

    @Override
    public List<ProductBox> findByBoxType(BoxType boxType) {
        return productBoxRepository.findByBoxType(boxType);
    }

    @Override
    public List<ProductBox> findByBoxSize(BoxSize boxSize) {
        return productBoxRepository.findByBoxSize(boxSize);
    }
}
