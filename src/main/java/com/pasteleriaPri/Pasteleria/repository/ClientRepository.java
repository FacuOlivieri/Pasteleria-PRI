package com.pasteleriaPri.Pasteleria.repository;

import com.pasteleriaPri.Pasteleria.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}
