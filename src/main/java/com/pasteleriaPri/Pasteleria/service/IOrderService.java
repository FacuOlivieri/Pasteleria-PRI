package com.pasteleriaPri.Pasteleria.service;

import com.pasteleriaPri.Pasteleria.dto.OrderDTO;
import com.pasteleriaPri.Pasteleria.dto.OrderRequestDTO;
import com.pasteleriaPri.Pasteleria.entity.OrderState;

import java.util.List;
import java.util.Optional;

public interface IOrderService {
    OrderDTO createOrder(OrderRequestDTO orderRequestDTO);
    Optional<OrderDTO> findById(Long id);
    List<OrderDTO> findAll();
    void deleteById(Long id);
    List<OrderDTO> findByClientId(Long clientId);
    OrderDTO updateOrderState(Long id, OrderState orderState);
}
