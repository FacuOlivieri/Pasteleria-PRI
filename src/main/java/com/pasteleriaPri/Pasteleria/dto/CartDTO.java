package com.pasteleriaPri.Pasteleria.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartDTO {

    private List<CartItemDTO> itemsDTO;
    private List<ProductBoxDTO> productBoxesDTO;
    private Double totalDTO;
}