package com.pasteleriaPri.Pasteleria.service;

import com.pasteleriaPri.Pasteleria.dto.CartDTO;
import com.pasteleriaPri.Pasteleria.dto.CartItemDTO;
import com.pasteleriaPri.Pasteleria.dto.ProductDTO;
import com.pasteleriaPri.Pasteleria.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The cart is per-visitor, in-flight state: {@code Cart} is not an @Entity and
 * there is no CartRepository, so it lives in the HTTP session instead of the
 * database. {@code @SessionScope} makes Spring inject a proxy, which is what
 * lets the singleton web controllers hold a reference and still reach the
 * right instance for the visitor making the current request.
 *
 * Subtotals and the total are computed here, never in a Thymeleaf expression —
 * THYMELEAF-CONVENTIONS.md keeps computation out of templates.
 */
@Service
@SessionScope
public class CartService implements ICartService {

    @Autowired
    private IProductService productService;

    /** Product id -> quantity. LinkedHashMap so the cart page keeps the order things were added in. */
    private final Map<Long, Integer> quantitiesByProductId = new LinkedHashMap<>();

    @Override
    public void addProduct(Long productId, Integer quantity) {
        int amount = (quantity == null || quantity < 1) ? 1 : quantity;

        // Resolve through the service so a bad id fails here, at the point of the
        // action, instead of blowing up later when the cart page renders.
        // findById already throws NotFoundException when the row is missing.
        productService.findById(productId);

        quantitiesByProductId.merge(productId, amount, Integer::sum);
    }

    @Override
    public void removeProduct(Long productId) {
        quantitiesByProductId.remove(productId);
    }

    @Override
    public void clear() {
        quantitiesByProductId.clear();
    }

    @Override
    public CartDTO getCart() {
        List<CartItemDTO> items = new ArrayList<>();
        double total = 0d;

        for (Map.Entry<Long, Integer> entry : quantitiesByProductId.entrySet()) {
            ProductDTO product;
            try {
                product = productService.findById(entry.getKey()).orElse(null);
            } catch (NotFoundException e) {
                // The product was deleted while it sat in someone's session.
                // Skip the stale line rather than break the whole cart page.
                // Caught explicitly because NotFoundException extends
                // NullPointerException, so "catch (RuntimeException)" misses it.
                continue;
            }
            if (product == null) {
                continue;
            }

            int quantity = entry.getValue();
            double unitPrice = product.getProdPriceDTO() != null ? product.getProdPriceDTO() : 0d;
            double subtotal = unitPrice * quantity;
            total += subtotal;

            items.add(CartItemDTO.builder()
                    .productDTO(product)
                    .quantityDTO(quantity)
                    .subtotalDTO(subtotal)
                    .build());
        }

        return CartDTO.builder()
                .itemsDTO(items)
                .productBoxesDTO(Collections.emptyList())
                .totalDTO(total)
                .build();
    }

    @Override
    public int getItemCount() {
        return quantitiesByProductId.values().stream().mapToInt(Integer::intValue).sum();
    }
}