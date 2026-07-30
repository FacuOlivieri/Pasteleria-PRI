package com.pasteleriaPri.Pasteleria.service;

import com.pasteleriaPri.Pasteleria.dto.OrderDTO;
import com.pasteleriaPri.Pasteleria.dto.OrderProductDetailDTO;
import com.pasteleriaPri.Pasteleria.dto.OrderRequestDTO;
import com.pasteleriaPri.Pasteleria.entity.*;
import com.pasteleriaPri.Pasteleria.exception.NotFoundException;
import com.pasteleriaPri.Pasteleria.helpers.Mapper;
import com.pasteleriaPri.Pasteleria.repository.ClientRepository;
import com.pasteleriaPri.Pasteleria.repository.OrderRepository;
import com.pasteleriaPri.Pasteleria.repository.ProductRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public OrderDTO createOrder(OrderRequestDTO orderRequestDTO) {
        Client client = clientRepository.findById(orderRequestDTO.getClientIdDTO()).orElseThrow(() -> new NotFoundException("Client not found"));
        List<OrderProductDetail> orderDetails = new ArrayList<>();
        double total = 0.0;
        Order order = createNewOrder(orderRequestDTO, client);

        //Por cada producto que este en la request del Cliente, se crea la linea en la orden de la compra
        for (OrderProductDetailDTO productDetailDTO : orderRequestDTO.getProductDetailsDTO()) {
            Product product = productRepository.findById(productDetailDTO.getProductIdDTO())
                    .orElseThrow(() -> new NotFoundException("Product not found with id: " + productDetailDTO.getProductIdDTO()));


            //Calculo Subtotal del Producto
            double unitPrice = product.getProdPrice();
            double subtotal = unitPrice * productDetailDTO.getQuantityDTO();
            total += subtotal;

            //Creacion de la linea del Producto
            OrderProductDetail detail = OrderProductDetail.builder()
                    .product(product)
                    .quantity(productDetailDTO.getQuantityDTO())
                    .unitPrice(unitPrice)
                    .subtotal(subtotal)
                    .order(order)
                    .build();

            orderDetails.add(detail);
        }

        Payment payment = new Payment();
        payment.setPaymentAmount(total);

        order.setOrderDetails(orderDetails);
        order.setPayment(payment);

        return Mapper.toOrderDTO(orderRepository.save(order));
    }



    @Override
    public Optional<OrderDTO> findById(Long id) {
        return Optional.of(orderRepository.findById(id)
                .map(Mapper::toOrderDTO)
                .orElseThrow(() -> new NotFoundException("Order not found")));
    }

    @Override
    public List<OrderDTO> findAll() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDTO> orderDTOs = new ArrayList<>();
        for (Order order : orders) {
            orderDTOs.add(Mapper.toOrderDTO(order));
        }
        return orderDTOs;
    }

    @Override
    public void deleteById(Long id) {
        orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        orderRepository.deleteById(id);
    }

    @Override
    public List<OrderDTO> findByClientId(Long clientId) {
        List<Order> orders = orderRepository.findByClientIdClient(clientId);
        List<OrderDTO> orderDTOs = new ArrayList<>();
        for (Order order : orders) {
            orderDTOs.add(Mapper.toOrderDTO(order));
        }
        return orderDTOs;
    }

    @Override
    public OrderDTO updateOrderState(Long id, OrderState orderState) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        order.setOrderState(orderState);
        return Mapper.toOrderDTO(orderRepository.save(order));
    }



    /////////////////////////////////////// Métodos HELPERS  ///////////////////////////////////////

    private @NonNull Order createNewOrder(OrderRequestDTO orderRequestDTO, Client client) {
        Order order = new Order();
        order.setDestinationAddress(orderRequestDTO.getDestinationAddressDTO());
        order.setIsPaid(false);
        order.setOrderDate(LocalDate.now());
        order.setOrderState(OrderState.confirmation);
        order.setClient(client);
        order.setProductBoxes(new ArrayList<>());
        return order;
    }


}
