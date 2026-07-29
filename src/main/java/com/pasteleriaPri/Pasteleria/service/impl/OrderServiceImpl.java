package com.pasteleriaPri.Pasteleria.service.impl;

import com.pasteleriaPri.Pasteleria.entity.Order;
import com.pasteleriaPri.Pasteleria.entity.OrderState;
import com.pasteleriaPri.Pasteleria.repository.OrderRepository;
import com.pasteleriaPri.Pasteleria.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    @Override
    public Order update(Long id, Order order) {
        order.setIdOrder(id);
        return orderRepository.save(order);
    }

    @Override
    public List<Order> findByClientId(Long clientId) {
        return orderRepository.findByClientIdClient(clientId);
    }

    @Override
    public Order updateOrderState(Long id, OrderState orderState) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        order.setOrderState(orderState);
        return orderRepository.save(order);
    }
}
