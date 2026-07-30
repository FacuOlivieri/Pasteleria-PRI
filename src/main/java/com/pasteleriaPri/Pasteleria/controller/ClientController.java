package com.pasteleriaPri.Pasteleria.controller;

import com.pasteleriaPri.Pasteleria.dto.ClientDTO;
import com.pasteleriaPri.Pasteleria.entity.Client;
import com.pasteleriaPri.Pasteleria.helpers.Mapper;
import com.pasteleriaPri.Pasteleria.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping ("/api/pasteleria/client")
public class ClientController {

    @Autowired
    private ClientService clientService;


    @PostMapping("/create")
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO clientDTO) {
        ClientDTO newClientDTO = clientService.save(clientDTO);
        return ResponseEntity.created(URI.create("/api/pasteleria/client")).body(newClientDTO);
    }

    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        return ResponseEntity.ok(clientService.findAll());
    }


}
