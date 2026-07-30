package com.pasteleriaPri.Pasteleria.service;

import com.pasteleriaPri.Pasteleria.dto.ProductDTO;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    ProductDTO save(ProductDTO product);
    Optional<ProductDTO> findById(Long id);
    List<ProductDTO> findAll();
    void deleteById(Long id);
    ProductDTO update(Long id, ProductDTO product);
    List<ProductDTO> findAllByProductsByType(Long productTypeId);
}
