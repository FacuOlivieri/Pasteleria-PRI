package com.pasteleriaPri.Pasteleria.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Backs the sign-in form only.
 *
 * Deliberately not ClientDTO: that one carries eight fields plus the order list,
 * and binding a two-field form to it would let a crafted POST set address, phone
 * or username on the object the controller is about to read. A form object should
 * expose exactly the fields the form actually has.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginFormDTO {

    private String emailDTO;
    private String passwordDTO;

}
