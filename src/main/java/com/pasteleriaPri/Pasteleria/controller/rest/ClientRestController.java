package com.pasteleriaPri.Pasteleria.controller.rest;

import com.pasteleriaPri.Pasteleria.dto.ClientDTO;
import com.pasteleriaPri.Pasteleria.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping ("/api/pasteleria/client")
public class ClientRestController {

    @Autowired
    private ClientService clientService;


    @PostMapping("/create")
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO clientDTO) {
        ClientDTO newClientDTO = clientService.save(clientDTO);
        return ResponseEntity.created(URI.create("/api/pasteleria/client")).body(newClientDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ClientDTO>> getClientById(@PathVariable Long id) {
        Optional<ClientDTO> client = clientService.findById(id);

        if (client.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(clientService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAllClients() {

        List<ClientDTO> clientDTOs = clientService.findAll();
        if (clientDTOs.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(clientService.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        if (clientService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        clientService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id, @RequestBody ClientDTO clientDTO) {
        return ResponseEntity.ok(clientService.update(id, clientDTO));
    }

}
