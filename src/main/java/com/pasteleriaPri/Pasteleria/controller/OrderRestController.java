package com.pasteleriaPri.Pasteleria.controller;

import com.pasteleriaPri.Pasteleria.dto.OrderDTO;
import com.pasteleriaPri.Pasteleria.dto.OrderRequestDTO;
import com.pasteleriaPri.Pasteleria.entity.OrderState;
import com.pasteleriaPri.Pasteleria.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pasteleria/order")
public class OrderRestController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        OrderDTO newOrderDTO = orderService.createOrder(orderRequestDTO);
        return ResponseEntity.created(URI.create("/api/pasteleria/order")).body(newOrderDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.findById(id).get());
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(orderService.findByClientId(clientId));
    }

    @PatchMapping("/{id}/state")
    public ResponseEntity<OrderDTO> updateOrderState(@PathVariable Long id, @RequestParam OrderState orderState) {
        return ResponseEntity.ok(orderService.updateOrderState(id, orderState));
    }
}
