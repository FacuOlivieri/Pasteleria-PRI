package com.pasteleriaPri.Pasteleria.dto;

import com.pasteleriaPri.Pasteleria.entity.Product;
import com.pasteleriaPri.Pasteleria.entity.ProductBox;

import java.time.LocalDate;
import java.util.List;

public class OrderDTO {

    private Long idOrderDTO;
    private String destinationAddressDTO;
    private LocalDate orderDate;
    private ClientDTO clientDTO;
    private PaymentDTO paymentDTO;
    private List<ProductDTO> productsDTO;
    private List<ProductBoxDTO> productBoxesDTO;
}
