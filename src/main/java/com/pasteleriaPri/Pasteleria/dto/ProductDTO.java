package com.pasteleriaPri.Pasteleria.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductDTO {

    private Long idProductDTO;
    private String prodNameDTO;
    private Integer prodQuantityDTO;
    private Double prodPriceDTO;
    private String prodDescriptionDTO;
    private String imgDTO;
    private ProductTypeDTO productTypeDTO;
}
