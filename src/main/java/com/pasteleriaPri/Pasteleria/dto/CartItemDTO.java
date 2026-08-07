package com.pasteleriaPri.Pasteleria.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartItemDTO {

    private ProductDTO productDTO;
    private Integer quantityDTO;
    private Double subtotalDTO;
}