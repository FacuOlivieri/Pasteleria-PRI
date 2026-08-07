package com.pasteleriaPri.Pasteleria.service;

import com.pasteleriaPri.Pasteleria.dto.ContactFormDTO;
import com.pasteleriaPri.Pasteleria.dto.EmailDTO;
import jakarta.mail.MessagingException;

public interface IEmailService {
    void sendEmail(EmailDTO emailDTO) throws MessagingException;

    /** Delivers a home-page contact form submission to the shop inbox. */
    void sendContactMessage(ContactFormDTO contactFormDTO) throws MessagingException;
}