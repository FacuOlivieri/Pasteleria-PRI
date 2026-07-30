package com.pasteleriaPri.Pasteleria.service;

import com.pasteleriaPri.Pasteleria.dto.ProductBoxDTO;
import com.pasteleriaPri.Pasteleria.entity.BoxSize;
import com.pasteleriaPri.Pasteleria.entity.BoxType;

import java.util.List;
import java.util.Optional;

public interface IProductBoxService {
    ProductBoxDTO save(ProductBoxDTO productBoxDTO);
    Optional<ProductBoxDTO> findById(Long id);
    List<ProductBoxDTO> findAll();
    void deleteById(Long id);
    ProductBoxDTO update(Long id, ProductBoxDTO productBoxDTO);
    List<ProductBoxDTO> findByBoxType(BoxType boxType);
    List<ProductBoxDTO> findByBoxSize(BoxSize boxSize);
}
