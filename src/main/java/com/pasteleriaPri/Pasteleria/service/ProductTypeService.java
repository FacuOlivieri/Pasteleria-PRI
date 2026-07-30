package com.pasteleriaPri.Pasteleria.service;

import com.pasteleriaPri.Pasteleria.dto.ProductTypeDTO;
import com.pasteleriaPri.Pasteleria.entity.ProductType;
import com.pasteleriaPri.Pasteleria.exception.NotFoundException;
import com.pasteleriaPri.Pasteleria.helpers.Mapper;
import com.pasteleriaPri.Pasteleria.repository.ProductTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductTypeService implements IProductTypeService {

    @Autowired
    private ProductTypeRepository productTypeRepository;

    @Override
    public ProductTypeDTO save(ProductTypeDTO productTypeDTO) {
        ProductType newProductType = Mapper.toProductType(productTypeDTO);
        return Mapper.toProductTypeDTO(productTypeRepository.save(newProductType));
    }

    @Override
    public Optional<ProductTypeDTO> findById(Long id) {
        return Optional.of(productTypeRepository.findById(id)
                .map(Mapper::toProductTypeDTO)
                .orElseThrow(() -> new NotFoundException("Type of product not found")));
    }

    @Override
    public List<ProductTypeDTO> findAll() {
        List<ProductType> productTypes = productTypeRepository.findAll();
        List<ProductTypeDTO> productTypeDTOs = new ArrayList<>();
        for (ProductType productType : productTypes) {
            productTypeDTOs.add(Mapper.toProductTypeDTO(productType));
        }
        return productTypeDTOs;
    }

    @Override
    public void deleteById(Long id) {
        ProductType productType = productTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ProductType not found"));
        productTypeRepository.delete(productType);
    }

    @Override
    public ProductTypeDTO update(Long id, ProductTypeDTO productTypeDTO) {
        ProductType foundProductType = productTypeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ProductType not found"));

        foundProductType.setProductTypeName(productTypeDTO.getProductTypeNameDTO());
        productTypeRepository.save(foundProductType);

        return Mapper.toProductTypeDTO(foundProductType);
    }
}
