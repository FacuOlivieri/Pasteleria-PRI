package com.pasteleriaPri.Pasteleria.entity;

public interface Validator {
    Boolean validatePassword(String password);
    Boolean validatePaymentState(Boolean isPaid);
    Boolean validateEmptyCart(Cart cart);
    Boolean validateEmptyProductBox(ProductBox productBox);
}
