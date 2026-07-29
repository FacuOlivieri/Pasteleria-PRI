package com.pasteleriaPri.Pasteleria.service;

import com.pasteleriaPri.Pasteleria.dto.ClientDTO;
import com.pasteleriaPri.Pasteleria.entity.Client;
import com.pasteleriaPri.Pasteleria.helpers.Mapper;
import com.pasteleriaPri.Pasteleria.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
}
