package com.pasteleriaPri.Pasteleria.service;

import com.pasteleriaPri.Pasteleria.dto.ProductDTO;
import com.pasteleriaPri.Pasteleria.entity.Product;
import com.pasteleriaPri.Pasteleria.exception.NotFoundException;
import com.pasteleriaPri.Pasteleria.helpers.Mapper;
import com.pasteleriaPri.Pasteleria.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ProductDTO save(ProductDTO productDTO) {
        Product newProduct = Mapper.toProduct(productDTO);
        productRepository.save(newProduct);
        return Mapper.toProductDTO(newProduct);
    }

    // Product.productType is LAZY and Mapper.toProductDTO now reads it, so every
    // read path needs its own transaction. Without it the mapping happens after
    // the repository's transaction closed and only Spring Boot's open-in-view
    // default saves it — which is not there in a unit test or a scheduled job.
    @Override
    @Transactional(readOnly = true)
    public Optional<ProductDTO> findById(Long id) {
        return Optional.of(productRepository.findById(id).map(Mapper::toProductDTO).orElseThrow(() -> new NotFoundException("Product not found")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOS = new ArrayList<>();

        for(Product product : products) {
            ProductDTO productDTO = Mapper.toProductDTO(product);
            productDTOS.add(productDTO);
        }

        return productDTOS;
    }

    @Override
    public void deleteById(Long id) {
        if(productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else  {
            throw new NotFoundException("Product not found");
        }
    }

    @Override
    public ProductDTO update(Long id, ProductDTO product) {
        Product productToUpdate = productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));

        productToUpdate.setProdName(product.getProdNameDTO());
        productToUpdate.setProdPrice(product.getProdPriceDTO());
        productToUpdate.setProdQuantity(product.getProdQuantityDTO());
        productToUpdate.setProdDescription(product.getProdDescriptionDTO());
        productToUpdate.setImg(product.getImgDTO());
        productToUpdate.setProductType(Mapper.toProductType(product.getProductTypeDTO()));

        productRepository.save(productToUpdate);
        return Mapper.toProductDTO(productToUpdate);

    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> findAllByProductsByType(Long productTypeId) {
        List<Product> products = productRepository.findAllByProductType_IdProductType(productTypeId);
        List<ProductDTO> productDTOs = new ArrayList<>();
        for (Product product : products) {
            productDTOs.add(Mapper.toProductDTO(product));
        }
        return productDTOs;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> findAllByPriceRange(Double minPrice, Double maxPrice) {
        List<Product> products = productRepository.findByProdPriceBetweenOrderByProdPriceAsc(minPrice, maxPrice);
        List<ProductDTO> productDTOs = new ArrayList<>();
        for (Product product : products) {
            productDTOs.add(Mapper.toProductDTO(product));
        }
        return productDTOs;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> findAllByFilters(Long productTypeId, Double minPrice, Double maxPrice) {
        List<Product> products = productRepository.findAllByFilters(productTypeId, minPrice, maxPrice);
        List<ProductDTO> productDTOs = new ArrayList<>();
        for (Product product : products) {
            productDTOs.add(Mapper.toProductDTO(product));
        }
        return productDTOs;
    }
}
