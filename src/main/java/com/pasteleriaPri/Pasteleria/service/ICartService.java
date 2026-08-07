package com.pasteleriaPri.Pasteleria.service;

import com.pasteleriaPri.Pasteleria.dto.CartDTO;

public interface ICartService {

    /** Adds a product to the cart, or increases its quantity if already present. */
    void addProduct(Long productId, Integer quantity);

    /** Removes a cart line entirely, whatever its quantity. */
    void removeProduct(Long productId);

    /** Empties the cart. */
    void clear();

    /** Current cart with per-line subtotals and the total already computed. */
    CartDTO getCart();

    /** Sum of the quantities of every line, for the navbar badge. */
    int getItemCount();
}