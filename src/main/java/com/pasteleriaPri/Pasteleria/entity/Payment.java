package com.pasteleriaPri.Pasteleria.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    // Argentine banking destination account (CBU) - shared across all payments
    public static final Double CBU_DESTINATION = 0.0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPayment;

    private Double paymentAmount;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "payment")
    private Order order;
}
