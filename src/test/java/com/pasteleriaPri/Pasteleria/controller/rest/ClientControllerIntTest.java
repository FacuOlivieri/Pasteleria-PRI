package com.pasteleriaPri.Pasteleria.controller.rest;

import com.pasteleriaPri.Pasteleria.dto.ClientDTO;
import com.pasteleriaPri.Pasteleria.entity.Client;
import com.pasteleriaPri.Pasteleria.exception.NotFoundException;
import com.pasteleriaPri.Pasteleria.repository.ClientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class ClientControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository repo;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createClient() throws Exception {
        ClientDTO clientDTO = ClientDTO.builder()
                .usernameDTO("Facu")
                .surnameDTO("Lopez")
                .addressDTO("Jujuy 3030")
                .cityDTO("San Justo")
                .phoneDTO("15500300")
                .emailDTO("facu@gmail.com")
                .passwordDTO("1234")
                .ordersDTO(null)
                .build();



        MvcResult mvcResult = mockMvc.perform(
                post("/api/pasteleria/client/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(objectMapper.writeValueAsString(clientDTO))
        )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();


        Client clientInBD = repo.findAll().stream()
                .filter(client -> "Facu".equals(clientDTO.getUsernameDTO()))
                .filter(client -> "Lopez".equals(clientDTO.getSurnameDTO()))
                .filter(client -> "Jujuy 3030".equals(clientDTO.getAddressDTO()))
                .filter(client -> "email".equals(clientDTO.getEmailDTO()))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("CLIENT NOT FOUND"));



        assertThat(clientInBD.getIdClient()).isNotNull();
        assertThat(clientInBD.getUsername()).isEqualTo("Facu");
        assertThat(clientInBD.getSurname()).isEqualTo("Lopez");
        assertThat(clientInBD.getAddress()).isEqualTo("Jujuy 3030");
        assertThat(clientInBD.getCity()).isEqualTo("San Justo");
        assertThat(clientInBD.getPhone()).isEqualTo("15500300");
        assertThat(clientInBD.getEmail()).isEqualTo("facu@gmail.com");

    }


}
