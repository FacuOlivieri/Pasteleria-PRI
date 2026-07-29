package com.pasteleriaPri.Pasteleria.service;

import com.pasteleriaPri.Pasteleria.dto.ClientDTO;
import com.pasteleriaPri.Pasteleria.entity.Client;

import java.util.List;
import java.util.Optional;

public interface IClientService {
    ClientDTO save(ClientDTO client);
    Optional<Client> findById(Long id);
    List<Client> findAll();
    void deleteById(Long id);
    Client update(Long id, Client client);
}
