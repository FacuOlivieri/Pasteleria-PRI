package com.pasteleriaPri.Pasteleria.controller.rest;

import com.pasteleriaPri.Pasteleria.dto.ProductBoxDTO;
import com.pasteleriaPri.Pasteleria.entity.BoxSize;
import com.pasteleriaPri.Pasteleria.entity.BoxType;
import com.pasteleriaPri.Pasteleria.service.ProductBoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pasteleria/productBox")
public class ProductBoxRestController {

    @Autowired
    private ProductBoxService productBoxService;

    @PostMapping("/create")
    public ResponseEntity<ProductBoxDTO> createProductBox(@RequestBody ProductBoxDTO productBoxDTO) {
        ProductBoxDTO newProductBoxDTO = productBoxService.save(productBoxDTO);
        return ResponseEntity.created(URI.create("/api/pasteleria/productBox")).body(newProductBoxDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductBoxDTO> getProductBoxById(@PathVariable Long id) {
        return ResponseEntity.ok(productBoxService.findById(id).get());
    }

    @GetMapping
    public ResponseEntity<List<ProductBoxDTO>> getAllProductBoxes() {
        return ResponseEntity.ok(productBoxService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductBox(@PathVariable Long id) {
        productBoxService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductBoxDTO> updateProductBox(@PathVariable Long id, @RequestBody ProductBoxDTO productBoxDTO) {
        return ResponseEntity.ok(productBoxService.update(id, productBoxDTO));
    }

    @GetMapping("/type/{boxType}")
    public ResponseEntity<List<ProductBoxDTO>> getProductBoxesByType(@PathVariable BoxType boxType) {
        return ResponseEntity.ok(productBoxService.findByBoxType(boxType));
    }

    @GetMapping("/size/{boxSize}")
    public ResponseEntity<List<ProductBoxDTO>> getProductBoxesBySize(@PathVariable BoxSize boxSize) {
        return ResponseEntity.ok(productBoxService.findByBoxSize(boxSize));
    }
}
