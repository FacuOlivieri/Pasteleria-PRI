package com.pasteleriaPri.Pasteleria.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderProductDetailDTO {

    private Long productIdDTO;
    private String productNameDTO;
    private Integer quantityDTO;
    private Double unitPriceDTO;
    private Double subtotalDTO;
}
