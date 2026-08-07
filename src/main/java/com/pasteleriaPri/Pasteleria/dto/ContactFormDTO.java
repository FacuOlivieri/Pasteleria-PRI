package com.pasteleriaPri.Pasteleria.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContactFormDTO {

    private String senderNameDTO;
    private String senderEmailDTO;
    private String subjectDTO;
    private String messageDTO;
}