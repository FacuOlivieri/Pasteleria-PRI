package com.pasteleriaPri.Pasteleria.dto;

import lombok.*;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailDTO {

    private String addressee;
    private String subject;
    private String message;

}
