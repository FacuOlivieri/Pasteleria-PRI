package com.pasteleriaPri.Pasteleria.service;

import com.pasteleriaPri.Pasteleria.entity.Payment;

import java.util.List;
import java.util.Optional;

public interface IPaymentService {
    Payment save(Payment payment);
    Optional<Payment> findById(Long id);
    List<Payment> findAll();
    void deleteById(Long id);
    Payment update(Long id, Payment payment);
}
