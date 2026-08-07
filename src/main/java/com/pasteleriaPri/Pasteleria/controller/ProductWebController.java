package com.pasteleriaPri.Pasteleria.controller;

import com.pasteleriaPri.Pasteleria.dto.ProductDTO;
import com.pasteleriaPri.Pasteleria.service.IProductService;
import com.pasteleriaPri.Pasteleria.service.IProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/pasteleria/product")
public class ProductWebController {

    @Autowired
    private IProductService productService;

    @Autowired
    private IProductTypeService productTypeService;

    /**
     * Catalog. Every filter is optional and they combine, which is why this goes
     * through findAllByFilters instead of chaining the older single-criterion
     * methods. The submitted values are echoed back into the model so the filter
     * bar stays filled in after the page reloads.
     */
    @GetMapping
    public String list(@RequestParam(required = false) Long typeId,
                       @RequestParam(required = false) Double minPrice,
                       @RequestParam(required = false) Double maxPrice,
                       Model model) {
        model.addAttribute("products", productService.findAllByFilters(typeId, minPrice, maxPrice));
        model.addAttribute("productTypes", productTypeService.findAll());
        model.addAttribute("selectedTypeId", typeId);
        model.addAttribute("selectedMinPrice", minPrice);
        model.addAttribute("selectedMaxPrice", maxPrice);
        model.addAttribute("returnUrl", buildReturnUrl(typeId, minPrice, maxPrice));
        return "product/list";
    }

    /**
     String builder, de esta manera unimos la URL cada variable que aplicamos al filtro
     */
    private String buildReturnUrl(Long typeId, Double minPrice, Double maxPrice) {
        StringBuilder url = new StringBuilder("/pasteleria/product");
        String separator = "?";

        if (typeId != null) {
            url.append(separator).append("typeId=").append(typeId);
            separator = "&";
        }
        if (minPrice != null) {
            url.append(separator).append("minPrice=").append(minPrice);
            separator = "&";
        }
        if (maxPrice != null) {
            url.append(separator).append("maxPrice=").append(maxPrice);
        }
        return url.toString();
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        // NotFoundException is left to propagate on purpose. It extends
        // NullPointerException, and THYMELEAF-CONVENTIONS.md is explicit that this
        // gap deserves one shared @ControllerAdvice, not an ad-hoc catch per
        // controller. Flagged, not improvised.
        ProductDTO product = productService.findById(id).orElseThrow();
        model.addAttribute("product", product);
        return "product/detail";
    }
}