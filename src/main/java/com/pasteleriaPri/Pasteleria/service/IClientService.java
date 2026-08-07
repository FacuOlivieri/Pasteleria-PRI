package com.pasteleriaPri.Pasteleria.service;

import com.pasteleriaPri.Pasteleria.dto.ClientDTO;
import java.util.List;
import java.util.Optional;

public interface IClientService {
    ClientDTO save(ClientDTO clientDTO);
    Optional<ClientDTO> findById(Long id);
    List<ClientDTO> findAll();
    void deleteById(Long id);
    ClientDTO update(Long id, ClientDTO clientDTO);

    /** True when an account already uses this email. */
    boolean emailExists(String email);

    /**
     * Resolves a sign-in attempt.
     *
     * @return the matching client, or empty if the email is unknown OR the
     *         password is wrong. The two cases are intentionally not
     *         distinguished - see the implementation.
     */
    Optional<ClientDTO> login(String email, String rawPassword);

    /** Records that this client is signed in on the current session. */
    void recordLogin(ClientDTO clientDTO);

    /** The client signed in on the current session, or null when nobody is. */
    ClientDTO getLoggedClient();

    /** Signs the current session out. Safe to call when nobody is signed in. */
    void logout();
}
