package com.pasteleriaPri.Pasteleria.repository;

import com.pasteleriaPri.Pasteleria.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
