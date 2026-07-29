package com.pasteleriaPri.Pasteleria.repository;

import com.pasteleriaPri.Pasteleria.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByClientIdClient(Long clientId);
}
