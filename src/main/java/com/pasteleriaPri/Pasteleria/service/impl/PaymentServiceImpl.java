package com.pasteleriaPri.Pasteleria.service.impl;

import com.pasteleriaPri.Pasteleria.entity.Payment;
import com.pasteleriaPri.Pasteleria.repository.PaymentRepository;
import com.pasteleriaPri.Pasteleria.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return paymentRepository.findById(id);
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        paymentRepository.deleteById(id);
    }

    @Override
    public Payment update(Long id, Payment payment) {
        payment.setIdPayment(id);
        return paymentRepository.save(payment);
    }
}
