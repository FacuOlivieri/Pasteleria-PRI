package com.pasteleriaPri.Pasteleria.controller;

import com.pasteleriaPri.Pasteleria.dto.ClientDTO;
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
     */
    @PostMapping
    public String create(@ModelAttribute ClientDTO clientDTO, RedirectAttributes redirectAttributes) {
        clientService.save(clientDTO);
        redirectAttributes.addFlashAttribute("accountCreated",
                "Your account was created. Welcome to Pasteleria Pri.");
        return "redirect:/pasteleria";
    }
}