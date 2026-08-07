package com.pasteleriaPri.Pasteleria.controller.web;

import com.pasteleriaPri.Pasteleria.dto.ClientDTO;
import com.pasteleriaPri.Pasteleria.exception.DuplicateEmailException;
import com.pasteleriaPri.Pasteleria.service.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pasteleria/client")
public class ClientWebController {

    @Autowired
    private IClientService clientService;

    @GetMapping("/new")
    public String createForm(Model model) {
        if (!model.containsAttribute("clientDTO")) {
            model.addAttribute("clientDTO", new ClientDTO());
        }
        return "client/form";
    }

    /**
     * WARNING: ClientDTO.passwordDTO is stored exactly as typed. There is no
     * PasswordEncoder and no Spring Security anywhere in this project yet, so
     * this saves plaintext passwords. Fine for a local demo, not for anything
     * real — hashing belongs in ClientService before this ever goes live.
     * LoginWebController compares those same plaintext values.
     */
    @PostMapping
    public String create(@ModelAttribute ClientDTO clientDTO, RedirectAttributes redirectAttributes) {
        try {
            clientService.save(clientDTO);
        } catch (DuplicateEmailException e) {
            // Email is the login identifier, so a second account on the same address
            // cannot be allowed. Send the form back filled in rather than making them
            // retype everything — same flash-attribute pattern as HomeWebController.
            redirectAttributes.addFlashAttribute("registerError", e.getMessage());
            redirectAttributes.addFlashAttribute("clientDTO", clientDTO);
            return "redirect:/pasteleria/client/new";
        }

        // Straight to the sign-in page rather than home: the account exists but the
        // visitor is not signed in yet, and this puts them where they can act on it.
        redirectAttributes.addFlashAttribute("loginNotice",
                "Your account was created. Sign in to continue.");
        return "redirect:/pasteleria/login";
    }
}