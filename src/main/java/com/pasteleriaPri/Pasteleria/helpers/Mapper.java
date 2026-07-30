package com.pasteleriaPri.Pasteleria.helpers;

import com.pasteleriaPri.Pasteleria.dto.*;
import com.pasteleriaPri.Pasteleria.entity.*;

import java.util.Collections;

import java.util.stream.Collectors;

public class Mapper {

    public static ClientDTO toClientDTO(Client client) {
        return ClientDTO.builder()
                .usernameDTO(client.getUsername())
                .surnameDTO(client.getSurname())
                .emailDTO(client.getEmail())
                .passwordDTO(client.getPassword())
                .phoneDTO(client.getPhone())
                .addressDTO(client.getAddress())
                .cityDTO(client.getCity())
                .build();
    }

    public static Client toClient(ClientDTO clientDTO) {
        return Client.builder()
                .username(clientDTO.getUsernameDTO())
                .surname(clientDTO.getSurnameDTO())
                .email(clientDTO.getEmailDTO())
                .password(clientDTO.getPasswordDTO())
                .phone(clientDTO.getPhoneDTO())
                .address(clientDTO.getAddressDTO())
                .city(clientDTO.getCityDTO())
                .build();
    }

    public static ProductDTO toProductDTO(Product product) {
        return ProductDTO.builder()
                .prodNameDTO(product.getProdName())
                .prodQuantityDTO(product.getProdQuantity())
                .prodPriceDTO(product.getProdPrice())
                .prodDescriptionDTO(product.getProdDescription())
                .imgDTO(product.getImg())
                .build();
    }

    public static Product toProduct(ProductDTO productDTO) {
        return Product.builder()
                .prodName(productDTO.getProdNameDTO())
                .prodQuantity(productDTO.getProdQuantityDTO())
                .prodPrice(productDTO.getProdPriceDTO())
                .prodDescription(productDTO.getProdDescriptionDTO())
                .img(productDTO.getImgDTO())
                .build();
    }

    public static PaymentDTO toPaymentDTO(Payment payment) {
        return PaymentDTO.builder()
                .paymentAmountDTO(payment.getPaymentAmount())
                .paymentTypeDTO(payment.getPaymentType())
                .build();
    }

    public static Payment toPayment(PaymentDTO paymentDTO) {
        Payment payment = new Payment();
        payment.setPaymentAmount(paymentDTO.getPaymentAmountDTO());
        payment.setPaymentType(paymentDTO.getPaymentTypeDTO());
        return payment;
    }

    public static ProductBoxDTO toProductBoxDTO(ProductBox productBox) {
        return ProductBoxDTO.builder()
                .totalPriceDTO(productBox.getTotalPrice())
                .boxTypeDTO(productBox.getBoxType())
                .boxSizeDTO(productBox.getBoxSize())
                .productsDTO(productBox.getProducts().stream()
                        .map(Mapper::toProductDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    public static ProductBox toProductBox(ProductBoxDTO productBoxDTO) {
        ProductBox productBox = new ProductBox();
        productBox.setTotalPrice(productBoxDTO.getTotalPriceDTO());
        productBox.setBoxType(productBoxDTO.getBoxTypeDTO());
        productBox.setBoxSize(productBoxDTO.getBoxSizeDTO());
        return productBox;
    }

    public static OrderProductDetailDTO toOrderDetailProductDTO(OrderProductDetail detail) {
        return OrderProductDetailDTO.builder()
                .productNameDTO(detail.getProduct().getProdName())
                .quantityDTO(detail.getQuantity())
                .unitPriceDTO(detail.getUnitPrice())
                .subtotalDTO(detail.getSubtotal())
                .build();
    }

    public static OrderDTO toOrderDTO(Order order) {
        return OrderDTO.builder()
                .destinationAddressDTO(order.getDestinationAddress())
                .isPaidDTO(order.getIsPaid())
                .orderDateDTO(order.getOrderDate())
                .orderStateDTO(order.getOrderState())
                .clientDTO(order.getClient() != null ? toClientDTO(order.getClient()) : null)
                .paymentDTO(order.getPayment() != null ? toPaymentDTO(order.getPayment()) : null)
                .orderDetailsDTO(order.getOrderDetails() != null
                        ? order.getOrderDetails().stream().map(Mapper::toOrderDetailProductDTO).collect(Collectors.toList())
                        : Collections.emptyList())
                .productBoxesDTO(order.getProductBoxes() != null
                        ? order.getProductBoxes().stream().map(Mapper::toProductBoxDTO).collect(Collectors.toList())
                        : Collections.emptyList())
                .build();
    }

    public static Order toOrder(OrderDTO orderDTO) {
        Order order = new Order();
        order.setDestinationAddress(orderDTO.getDestinationAddressDTO());
        order.setIsPaid(orderDTO.getIsPaidDTO());
        order.setOrderDate(orderDTO.getOrderDateDTO());
        order.setOrderState(orderDTO.getOrderStateDTO());
        return order;
    }

    public static ProductTypeDTO toProductTypeDTO(ProductType productType) {
        return ProductTypeDTO.builder()
                .productTypeNameDTO(productType.getProductTypeName())
                .build();
    }

    public static ProductType toProductType(ProductTypeDTO productTypeDTO) {
        ProductType productType = new ProductType();
        productType.setProductTypeName(productTypeDTO.getProductTypeNameDTO());
        return productType;
    }

    public static CartDTO toCartDTO(Cart cart) {
        return CartDTO.builder()
                .productsDTO(cart.getProducts().stream()
                        .map(Mapper::toProductDTO)
                        .collect(Collectors.toList()))
                .productBoxesDTO(cart.getProductBoxes().stream()
                        .map(Mapper::toProductBoxDTO)
                        .collect(Collectors.toList()))
                .build();
    }

    public static Cart toCart(CartDTO cartDTO) {
        Cart cart = new Cart();
        cart.setProducts(cartDTO.getProductsDTO().stream()
                .map(Mapper::toProduct)
                .collect(Collectors.toList()));
        cart.setProductBoxes(cartDTO.getProductBoxesDTO().stream()
                .map(Mapper::toProductBox)
                .collect(Collectors.toList()));
        return cart;
    }
}
