package com.pasteleriaPri.Pasteleria.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idClient;

    private String username;
    private String surname;
    private String address;
    private String city;
    private String phone;

    // Email is the login identifier, so the database has to guarantee it is unique.
    // Doing this check only in Java would still let two concurrent registrations
    // slip through and leave findByEmail with two rows to choose from.
    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders;
}
