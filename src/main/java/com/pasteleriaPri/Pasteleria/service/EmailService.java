package com.pasteleriaPri.Pasteleria.service;

import com.pasteleriaPri.Pasteleria.dto.ContactFormDTO;
import com.pasteleriaPri.Pasteleria.dto.EmailDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

@Service
public class EmailService implements IEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    /** The shop's own inbox — same account the JavaMailSender authenticates with. */
    @Value("${email.username}")
    private String shopInbox;

    @Override
    public void sendEmail(EmailDTO emailDTO) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(emailDTO.getAddressee());
        helper.setSubject(emailDTO.getSubject());
        helper.setText(emailDTO.getMessage(), false);

        mailSender.send(message);
    }

    /**
     * A contact form is not mail *to* the visitor, it is mail *about* the visitor:
     * it goes to the shop inbox, with the visitor's own address as Reply-To so a
     * plain "reply" in Gmail answers the right person.
     */
    @Override
    public void sendContactMessage(ContactFormDTO contactFormDTO) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(shopInbox);
        helper.setReplyTo(contactFormDTO.getSenderEmailDTO());
        helper.setSubject("[Web] " + contactFormDTO.getSubjectDTO());
        helper.setText(buildContactBody(contactFormDTO), false);

        mailSender.send(message);
    }

    private String buildContactBody(ContactFormDTO contactFormDTO) {
        return """
                New message from the website contact form.

                Name:  %s
                Email: %s

                %s
                """.formatted(
                contactFormDTO.getSenderNameDTO(),
                contactFormDTO.getSenderEmailDTO(),
                contactFormDTO.getMessageDTO());
    }
}