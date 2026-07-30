package com.pasteleriaPri.Pasteleria.controller;

import com.pasteleriaPri.Pasteleria.dto.ProductTypeDTO;
import com.pasteleriaPri.Pasteleria.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pasteleria/productType")
public class ProductTypeController {

    @Autowired
    private ProductTypeService productTypeService;

    @PostMapping("/create")
    public ResponseEntity<ProductTypeDTO> createProductType(@RequestBody ProductTypeDTO productTypeDTO) {
        ProductTypeDTO newProductTypeDTO = productTypeService.save(productTypeDTO);
        return ResponseEntity.created(URI.create("/api/pasteleria/productType")).body(newProductTypeDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductTypeDTO> getProductTypeById(@PathVariable Long id) {
        return ResponseEntity.ok(productTypeService.findById(id).get());
    }

    @GetMapping
    public ResponseEntity<List<ProductTypeDTO>> getAllProductTypes() {
        return ResponseEntity.ok(productTypeService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductType(@PathVariable Long id) {
        productTypeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductTypeDTO> updateProductType(@PathVariable Long id, @RequestBody ProductTypeDTO productTypeDTO) {
        return ResponseEntity.ok(productTypeService.update(id, productTypeDTO));
    }
}
