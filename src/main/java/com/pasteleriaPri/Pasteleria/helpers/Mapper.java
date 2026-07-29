package com.pasteleriaPri.Pasteleria.helpers;

import com.pasteleriaPri.Pasteleria.dto.ClientDTO;
import com.pasteleriaPri.Pasteleria.entity.Client;

public class Mapper {

    public static ClientDTO toClientDTO(Client client) {
        return ClientDTO.builder()
                .usernameDTO(client.getUsername())
                .surnameDTO(client.getSurname())
                .emailDTO(client.getEmail())
                .passwordDTO(client.getPassword())
                .phoneDTO(client.getPhone())
                .addressDTO(client.getAddress())
                .cityDTO(client.getCity())
                .build();
    }

    public static Client toClient(ClientDTO clientDTO) {
        return Client.builder()
                .username(clientDTO.getUsernameDTO())
                .surname(clientDTO.getSurnameDTO())
                .email(clientDTO.getEmailDTO())
                .password(clientDTO.getPasswordDTO())
                .phone(clientDTO.getPhoneDTO())
                .address(clientDTO.getAddressDTO())
                .city(clientDTO.getCityDTO())
                .build();
    }



}
