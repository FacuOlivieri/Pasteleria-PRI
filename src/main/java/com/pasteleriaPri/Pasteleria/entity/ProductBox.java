package com.pasteleriaPri.Pasteleria.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "product_boxes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductBox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProductBox;

    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private BoxType boxType;

    @Enumerated(EnumType.STRING)
    private BoxSize boxSize;

    @ManyToMany
    @JoinTable(
            name = "product_box_products",
            joinColumns = @JoinColumn(name = "product_box_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;

    @ManyToMany(mappedBy = "productBoxes")
    private List<Order> orders;
}
