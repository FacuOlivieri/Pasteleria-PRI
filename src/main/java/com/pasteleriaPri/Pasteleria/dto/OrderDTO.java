package com.pasteleriaPri.Pasteleria.dto;

import com.pasteleriaPri.Pasteleria.entity.OrderState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDTO {

    private String destinationAddressDTO;
    private Boolean isPaidDTO;
    private LocalDate orderDateDTO;
    private OrderState orderStateDTO;
    private ClientDTO clientDTO;
    private PaymentDTO paymentDTO;
    private List<OrderProductDetailDTO> orderDetailsDTO;
    private List<ProductBoxDTO> productBoxesDTO;
}
