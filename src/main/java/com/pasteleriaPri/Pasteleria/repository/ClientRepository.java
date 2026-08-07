package com.pasteleriaPri.Pasteleria.repository;

import com.pasteleriaPri.Pasteleria.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    /**
     * Used by login and by the duplicate-registration guard. No @Query needed:
     * Spring Data derives the query from the method name, matching the "email"
     * field on Client.
     */
    Optional<Client> findByEmail(String email);
}
