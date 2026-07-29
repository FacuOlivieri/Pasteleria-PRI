package com.pasteleriaPri.Pasteleria.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ClientDTO {

    private String usernameDTO;
    private String surnameDTO;
    private String addressDTO;
    private String cityDTO;
    private String phoneDTO;
    private String emailDTO;
    private String passwordDTO;

    //Faltan las OrdersDTO
    //private List<Order> ordersDTO;

}
