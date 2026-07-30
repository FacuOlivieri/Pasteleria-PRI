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
public class OrderRequestDTO {

    private String destinationAddressDTO;
    private Long clientIdDTO;
    private List<OrderProductDetailDTO> productDetailsDTO;
}
