package com.pasteleriaPri.Pasteleria.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientDTO {

    private String usernameDTO;
    private String surnameDTO;
    private String addressDTO;
    private String cityDTO;
    private String phoneDTO;
    private String emailDTO;
    private String passwordDTO;
    private List<OrderDTO> ordersDTO;

}
