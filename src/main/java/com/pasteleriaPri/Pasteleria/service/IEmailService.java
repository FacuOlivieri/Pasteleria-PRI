package com.pasteleriaPri.Pasteleria.service;

import com.pasteleriaPri.Pasteleria.dto.EmailDTO;
import jakarta.mail.MessagingException;

public interface IEmailService {
    void sendEmail(EmailDTO emailDTO) throws MessagingException;
}
