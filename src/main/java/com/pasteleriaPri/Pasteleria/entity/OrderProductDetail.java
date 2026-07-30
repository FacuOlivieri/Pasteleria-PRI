package com.pasteleriaPri.Pasteleria.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_detail_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrderDetail;

    private Integer quantity;
    private Double unitPrice;
    private Double subtotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;
}
