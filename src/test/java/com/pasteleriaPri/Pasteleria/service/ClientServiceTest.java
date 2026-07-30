package com.pasteleriaPri.Pasteleria.service;

import com.pasteleriaPri.Pasteleria.dto.ClientDTO;
import com.pasteleriaPri.Pasteleria.entity.Client;
import com.pasteleriaPri.Pasteleria.exception.NotFoundException;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
        when(clientRepoMock.save(any(Client.class))).thenReturn(client);

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

    @Test
    void getClientById(){
        Client client = Client.builder()
                .idClient(1L)
                .username("Facundo")
                .surname("Lopez")
                .password("1234")
                .email("facu@gmail.com")
                .build();
        when(clientRepoMock.findById(1L)).thenReturn(Optional.of(client));

        Optional<Client> result = clientRepoMock.findById(1L);
        verify(clientRepoMock).findById(1L);
        verifyNoMoreInteractions(clientRepoMock);

        assertEquals("Facundo", result.get().getUsername());
        assertEquals("Lopez", result.get().getSurname());
        assertEquals("1234", result.get().getPassword());
        assertEquals("facu@gmail.com", result.get().getEmail());

    }

    @Test
    void getExceptionWhenClientNotFound(){
        assertThrows(NotFoundException.class, () -> clientService.findById(1L));
    }

    @Test
    void getAllClients(){
        Client client1 = new Client(1L,"Facu","Lopez","Jujuy 3030", "San Justo", "15300200","facu@gmail.com", "1234", null);
        Client client2 = new Client(2L,"Fede","Lopez","Jujuy 3030", "San Justo", "15450200","fede@gmail.com", "5263", null);
        when(clientRepoMock.findAll()).thenReturn(Arrays.asList(client1,client2));

        List<ClientDTO> lista = clientService.findAll();
        verify(clientRepoMock).findAll();
        verifyNoMoreInteractions(clientRepoMock);
        assertEquals("Facu", lista.get(0).getUsernameDTO());
        assertEquals("Lopez", lista.get(0).getSurnameDTO());
        assertEquals("Fede", lista.get(1).getUsernameDTO());
        assertEquals("15450200", lista.get(1).getPhoneDTO());
    }

    @Test
    void deleteClientSuccessfully(){
        Client client1 = new Client(1L,"Facu","Lopez","Jujuy 3030", "San Justo", "15300200","facu@gmail.com", "1234", null);
        when(clientRepoMock.findById(1L)).thenReturn(Optional.of(client1));

        clientService.deleteById(1L);

        verify(clientRepoMock).findById(1L);
        verify(clientRepoMock).delete(client1);
        verifyNoMoreInteractions(clientRepoMock);

    }

}



