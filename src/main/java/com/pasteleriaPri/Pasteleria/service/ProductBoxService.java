package com.pasteleriaPri.Pasteleria.service;

import com.pasteleriaPri.Pasteleria.dto.ProductBoxDTO;
import com.pasteleriaPri.Pasteleria.entity.BoxSize;
import com.pasteleriaPri.Pasteleria.entity.BoxType;
import com.pasteleriaPri.Pasteleria.entity.ProductBox;
import com.pasteleriaPri.Pasteleria.exception.NotFoundException;
import com.pasteleriaPri.Pasteleria.helpers.Mapper;
import com.pasteleriaPri.Pasteleria.repository.ProductBoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductBoxService implements IProductBoxService {

    @Autowired
    private ProductBoxRepository productBoxRepository;

    @Override
    public ProductBoxDTO save(ProductBoxDTO productBoxDTO) {
        ProductBox newProductBox = Mapper.toProductBox(productBoxDTO);
        return Mapper.toProductBoxDTO(productBoxRepository.save(newProductBox));
    }

    @Override
    public Optional<ProductBoxDTO> findById(Long id) {
        return Optional.of(productBoxRepository.findById(id)
                .map(Mapper::toProductBoxDTO)
                .orElseThrow(() -> new NotFoundException("ProductBox not found")));
    }

    @Override
    public List<ProductBoxDTO> findAll() {
        List<ProductBox> productBoxes = productBoxRepository.findAll();
        List<ProductBoxDTO> productBoxDTOs = new ArrayList<>();
        for (ProductBox productBox : productBoxes) {
            productBoxDTOs.add(Mapper.toProductBoxDTO(productBox));
        }
        return productBoxDTOs;
    }

    @Override
    public void deleteById(Long id) {
        ProductBox productBox = productBoxRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ProductBox not found"));
        productBoxRepository.delete(productBox);
    }

    @Override
    public ProductBoxDTO update(Long id, ProductBoxDTO productBoxDTO) {
        ProductBox foundProductBox = productBoxRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("ProductBox not found"));

        foundProductBox.setTotalPrice(productBoxDTO.getTotalPriceDTO());
        foundProductBox.setBoxType(productBoxDTO.getBoxTypeDTO());
        foundProductBox.setBoxSize(productBoxDTO.getBoxSizeDTO());
        productBoxRepository.save(foundProductBox);

        return Mapper.toProductBoxDTO(foundProductBox);
    }

    @Override
    public List<ProductBoxDTO> findByBoxType(BoxType boxType) {
        List<ProductBox> productBoxes = productBoxRepository.findByBoxType(boxType);
        List<ProductBoxDTO> productBoxDTOs = new ArrayList<>();
        for (ProductBox productBox : productBoxes) {
            productBoxDTOs.add(Mapper.toProductBoxDTO(productBox));
        }
        return productBoxDTOs;
    }

    @Override
    public List<ProductBoxDTO> findByBoxSize(BoxSize boxSize) {
        List<ProductBox> productBoxes = productBoxRepository.findByBoxSize(boxSize);
        List<ProductBoxDTO> productBoxDTOs = new ArrayList<>();
        for (ProductBox productBox : productBoxes) {
            productBoxDTOs.add(Mapper.toProductBoxDTO(productBox));
        }
        return productBoxDTOs;
    }
}
