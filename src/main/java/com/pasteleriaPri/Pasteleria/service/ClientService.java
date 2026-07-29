package com.pasteleriaPri.Pasteleria.service;

import com.pasteleriaPri.Pasteleria.entity.Client;

import java.util.List;
import java.util.Optional;

public class ClientService implements IClientService{
    @Override
    public Client save(Client client) {
        return null;
    }

    @Override
    public Optional<Client> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Client> findAll() {
        return List.of();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public Client update(Long id, Client client) {
        return null;
    }
}
