package com.pasteleriaPri.Pasteleria.service;

import com.pasteleriaPri.Pasteleria.dto.ClientDTO;
import java.util.List;
import java.util.Optional;

public interface IClientService {
    ClientDTO save(ClientDTO clientDTO);
    Optional<ClientDTO> findById(Long id);
    List<ClientDTO> findAll();
    void deleteById(Long id);
    ClientDTO update(Long id, ClientDTO clientDTO);
}
