package com.pasteleriaPri.Pasteleria.service;

import com.pasteleriaPri.Pasteleria.dto.ClientDTO;
import com.pasteleriaPri.Pasteleria.entity.Client;
import com.pasteleriaPri.Pasteleria.exception.DuplicateEmailException;
import com.pasteleriaPri.Pasteleria.exception.NotFoundException;
import com.pasteleriaPri.Pasteleria.helpers.Mapper;
import com.pasteleriaPri.Pasteleria.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Session-scoped, the same way CartService is: {@code @SessionScope} makes
 * Spring inject a proxy, so every visitor's HTTP session gets its own
 * loggedClient field without any manual session-attribute plumbing. The CRUD
 * methods below (save, findAll, update, ...) are unaffected by this - they
 * are stateless and always go through the shared ClientRepository - only
 * recordLogin/getLoggedClient/logout actually use the per-session field.
 */
@Service
@SessionScope
public class ClientService implements IClientService{

    @Autowired
    private ClientRepository clientRepository;

    /** Who is signed in on this session. Null when nobody is. Identity only, not authorization. */
    private ClientDTO loggedClient;


    @Override
    public ClientDTO save(ClientDTO clientDTO) {
        // Guard before the insert so the caller gets a meaningful exception instead
        // of a raw constraint-violation stack trace from the unique index on email.
        // The index is still the real safety net - this check only makes the common
        // case readable.
        if (emailExists(clientDTO.getEmailDTO())) {
            throw new DuplicateEmailException("An account already exists with that email");
        }
        Client newClient = Mapper.toClient(clientDTO);
        return Mapper.toClientDTO(clientRepository.save(newClient));
    }

    @Override
    public boolean emailExists(String email) {
        return clientRepository.findByEmail(email).isPresent();
    }

    @Override
    public Optional<ClientDTO> login(String email, String rawPassword) {
        return clientRepository.findByEmail(email)
                // TODO: passwords are stored and compared in plaintext. There is no
                // PasswordEncoder and no Spring Security in this project yet - the same
                // gap ClientWebController warns about. When hashing arrives, this line
                // becomes passwordEncoder.matches(rawPassword, client.getPassword())
                // and nothing else here has to change.
                .filter(client -> client.getPassword() != null
                        && client.getPassword().equals(rawPassword))
                .map(Mapper::toClientDTO);
        // Returning empty for "no such email" and for "wrong password" alike is
        // deliberate. Telling them apart would turn the login form into a way to
        // check which addresses are registered.
    }

    @Override
    public void recordLogin(ClientDTO clientDTO) {
        // Strip the password before it goes into the session field. login() above
        // copies it straight out of the entity via Mapper, and this object gets
        // handed to every template through GlobalWebModelAdvice - there is no
        // reason for a password to travel that far. Copied rather than mutated so
        // we do not clear the field on an object the caller may still be using.
        this.loggedClient = clientDTO == null ? null : ClientDTO.builder()
                .idClientDTO(clientDTO.getIdClientDTO())
                .usernameDTO(clientDTO.getUsernameDTO())
                .surnameDTO(clientDTO.getSurnameDTO())
                .emailDTO(clientDTO.getEmailDTO())
                .phoneDTO(clientDTO.getPhoneDTO())
                .addressDTO(clientDTO.getAddressDTO())
                .cityDTO(clientDTO.getCityDTO())
                .build();
    }

    @Override
    public ClientDTO getLoggedClient() {
        return loggedClient;
    }

    @Override
    public void logout() {
        this.loggedClient = null;
    }

    @Override
    public Optional<ClientDTO> findById(Long id) {
        return Optional.of(clientRepository.findById(id).map(Mapper::toClientDTO).orElseThrow(() -> new NotFoundException("Client Not Found")));
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
        foundClient.setAddress(clientDTO.getAddressDTO());
        foundClient.setPhone(clientDTO.getPhoneDTO());
        clientRepository.save(foundClient);

        return Mapper.toClientDTO(foundClient);
    }
}
