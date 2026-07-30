package com.pasteleriaPri.Pasteleria.service;

import com.pasteleriaPri.Pasteleria.dto.ClientDTO;
import com.pasteleriaPri.Pasteleria.entity.Client;
import com.pasteleriaPri.Pasteleria.helpers.Mapper;
import com.pasteleriaPri.Pasteleria.repository.ClientRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepoMock;

    @InjectMocks
    private ClientService clientService;

    @Captor
    private ArgumentCaptor<Client> clientCaptor;


    @Test
    void createClient() {
        //Arrange
        ClientDTO clientDTO = ClientDTO.builder()
                .usernameDTO("Facundo")
                .surnameDTO("Lopez")
                .passwordDTO("1234")
                .emailDTO("facu@gmail.com")
                .build();

        Client client = Client.builder().idClient(1L)
                .username("Facundo")
                .surname("Lopez")
                .email("facu@gmail.com")
                .password("1234")
                .build();
        when(clientRepoMock.save(Mapper.toClient(clientDTO))).thenReturn(client);

        ClientDTO newClient = clientService.save(clientDTO);

        verify(clientRepoMock).save(clientCaptor.capture());
        assertNotNull(newClient);
        assertEquals("Facundo", clientCaptor.getValue().getUsername());
        assertEquals("Lopez", clientCaptor.getValue().getSurname());
        assertEquals("1234", clientCaptor.getValue().getPassword());
        assertEquals("facu@gmail.com", clientCaptor.getValue().getEmail());

    }


    @Test
    void updateClient() {
        Client client = Client.builder()
                .idClient(1L)
                .username("Facundo")
                .surname("Lopez")
                .password("1234")
                .email("facu@gmail.com")
                .build();

        ClientDTO updatedClientDTO = ClientDTO.builder()
                .usernameDTO("Fede")
                .surnameDTO("Gomez")
                .passwordDTO("5678")
                .emailDTO("fede@gmail.com")
                .build();

        when(clientRepoMock.findById(1L)).thenReturn(Optional.ofNullable(client));

        clientService.update(1L,updatedClientDTO);
        verify(clientRepoMock).save(clientCaptor.capture());
        verify(clientRepoMock).findById(1L);
        verifyNoMoreInteractions(clientRepoMock);
        assertEquals("Fede", clientCaptor.getValue().getUsername());
        assertEquals("Gomez", clientCaptor.getValue().getSurname());
        assertEquals("5678", clientCaptor.getValue().getPassword());
        assertEquals("fede@gmail.com", clientCaptor.getValue().getEmail());



    }
}
