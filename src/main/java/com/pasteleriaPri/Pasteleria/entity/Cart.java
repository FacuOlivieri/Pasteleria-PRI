package com.pasteleriaPri.Pasteleria.entity;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    private List<Product> products;
    private List<ProductBox> productBoxes;
}
