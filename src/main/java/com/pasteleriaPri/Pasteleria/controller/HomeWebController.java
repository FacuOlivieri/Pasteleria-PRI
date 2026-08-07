package com.pasteleriaPri.Pasteleria.controller;

import com.pasteleriaPri.Pasteleria.dto.ContactFormDTO;
import com.pasteleriaPri.Pasteleria.service.IEmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Landing page and its contact form.
 *
 * Deviation from THYMELEAF-CONVENTIONS.md, on purpose: there is no class-level
 * {@code @RequestMapping("/pasteleria/<resource>")} here, because the home page
 * is not a resource. Routes are declared per method instead.
 */
@Controller
public class HomeWebController {

    @Autowired
    private IEmailService emailService;

    @GetMapping({"/", "/pasteleria"})
    public String home(Model model) {
        // A failed submit redirects back with the filled DTO as a flash attribute,
        // so only seed an empty one when there is nothing to restore.
        if (!model.containsAttribute("contactFormDTO")) {
            model.addAttribute("contactFormDTO", new ContactFormDTO());
        }
        return "index";
    }

    @PostMapping("/pasteleria/contact")
    public String contact(@ModelAttribute ContactFormDTO contactFormDTO,
                          RedirectAttributes redirectAttributes) {
        try {
            emailService.sendContactMessage(contactFormDTO);
            redirectAttributes.addFlashAttribute("contactSuccess",
                    "Thanks for writing to us. We will get back to you soon.");
        } catch (MessagingException | RuntimeException e) {
            // MessagingException is checked; the mail server can also fail with an
            // unchecked MailException. Neither should reach the whitelabel error
            // page, so both come back as a message on the form.
            redirectAttributes.addFlashAttribute("contactError",
                    "We could not send your message right now. Please try again later.");
            redirectAttributes.addFlashAttribute("contactFormDTO", contactFormDTO);
        }
        return "redirect:/pasteleria#contacto";
    }
}