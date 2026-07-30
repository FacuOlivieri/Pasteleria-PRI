package com.pasteleriaPri.Pasteleria.service;

import com.pasteleriaPri.Pasteleria.dto.ClientDTO;
import com.pasteleriaPri.Pasteleria.entity.Client;
import com.pasteleriaPri.Pasteleria.exception.NotFoundException;
import com.pasteleriaPri.Pasteleria.helpers.Mapper;
import com.pasteleriaPri.Pasteleria.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClientService implements IClientService{

    @Autowired
    private ClientRepository clientRepository;


    @Override
    public ClientDTO save(ClientDTO clientDTO) {
        Client newClient = Mapper.toClient(clientDTO);
        return Mapper.toClientDTO(clientRepository.save(newClient));
    }

    @Override
    public Optional<ClientDTO> findById(Long id) {
        return clientRepository.findById(id).map(Mapper::toClientDTO);
    }

    @Override
    public List<ClientDTO> findAll() {
        List <Client> clients = clientRepository.findAll();
        List <ClientDTO> clientDTOs = new ArrayList<>();
        for (Client client : clients) {
            clientDTOs.add(Mapper.toClientDTO(client));
        }
        return clientDTOs;
    }

    @Override
    public void deleteById(Long id) {
        Client client = clientRepository.findById(id).orElseThrow(() -> new NotFoundException("Client not found"));
        clientRepository.delete(client);
    }

    @Override
    public ClientDTO update(Long id, ClientDTO clientDTO) {
        Client foundClient = clientRepository.findById(id).orElseThrow(() -> new NotFoundException("Client not found"));

        foundClient.setUsername(clientDTO.getUsernameDTO());
        foundClient.setSurname(clientDTO.getSurnameDTO());
        foundClient.setEmail(clientDTO.getEmailDTO());
        foundClient.setPassword(clientDTO.getPasswordDTO());
        foundClient.setCity(clientDTO.getCityDTO());
        foundClient.setOrders(clientDTO.getOrdersDTO());
        foundClient.setAddress(clientDTO.getAddressDTO());
        foundClient.setPhone(clientDTO.getPhoneDTO());
        clientRepository.save(foundClient);

        return Mapper.toClientDTO(foundClient);
    }
}
