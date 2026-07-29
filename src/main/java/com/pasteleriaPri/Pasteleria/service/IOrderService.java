package com.pasteleriaPri.Pasteleria.service;

import com.pasteleriaPri.Pasteleria.entity.Order;
import com.pasteleriaPri.Pasteleria.entity.OrderState;

import java.util.List;
import java.util.Optional;

public interface IOrderService {
    Order save(Order order);
    Optional<Order> findById(Long id);
    List<Order> findAll();
    void deleteById(Long id);
    Order update(Long id, Order order);
    List<Order> findByClientId(Long clientId);
    Order updateOrderState(Long id, OrderState orderState);
}
