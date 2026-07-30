package com.pasteleriaPri.Pasteleria.service;

import com.pasteleriaPri.Pasteleria.dto.ProductTypeDTO;

import java.util.List;
import java.util.Optional;

public interface IProductTypeService {
    ProductTypeDTO save(ProductTypeDTO productTypeDTO);
    Optional<ProductTypeDTO> findById(Long id);
    List<ProductTypeDTO> findAll();
    void deleteById(Long id);
    ProductTypeDTO update(Long id, ProductTypeDTO productTypeDTO);
}
