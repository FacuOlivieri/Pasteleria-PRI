package com.pasteleriaPri.Pasteleria.controller.web;

import com.pasteleriaPri.Pasteleria.dto.ClientDTO;
import com.pasteleriaPri.Pasteleria.dto.LoginFormDTO;
import com.pasteleriaPri.Pasteleria.service.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * Sign in and sign out.
 *
 * Deviation from THYMELEAF-CONVENTIONS.md, on purpose and for the same reason
 * HomeWebController takes it: there is no class-level
 * {@code @RequestMapping("/pasteleria/<resource>")} here, because signing in is
 * an action, not a resource. Routes are declared per method instead.
 */
@Controller
public class LoginWebController {

    @Autowired
    private IClientService clientService;

    @GetMapping("/pasteleria/login")
    public String loginForm(Model model) {
        // A failed attempt redirects back with the email as a flash attribute, so
        // only seed an empty form when there is nothing to restore.
        if (!model.containsAttribute("loginFormDTO")) {
            model.addAttribute("loginFormDTO", new LoginFormDTO());
        }
        return "client/login";
    }

    @PostMapping("/pasteleria/login")
    public String login(@ModelAttribute LoginFormDTO loginFormDTO,
                        RedirectAttributes redirectAttributes) {

        Optional<ClientDTO> client = clientService.login(
                loginFormDTO.getEmailDTO(), loginFormDTO.getPasswordDTO());

        if (client.isEmpty()) {
            // One message for both "no such email" and "wrong password". The service
            // already refuses to tell them apart; saying "no account with that email"
            // here would undo that and turn the form into a way to discover which
            // addresses are registered.
            redirectAttributes.addFlashAttribute("loginError",
                    "Email or password is not correct.");
            // Send the email back so they do not retype it, but never the password.
            redirectAttributes.addFlashAttribute("loginFormDTO",
                    LoginFormDTO.builder().emailDTO(loginFormDTO.getEmailDTO()).build());
            return "redirect:/pasteleria/login";
        }

        clientService.recordLogin(client.get());
        return "redirect:/pasteleria";
    }

    /**
     * POST, not GET, on purpose: a GET logout can be fired by any {@code <img>} tag
     * on any page, and browsers happily prefetch links. Signing someone out is a
     * state change, so it takes a form submit.
     */
    @PostMapping("/pasteleria/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        clientService.logout();
        redirectAttributes.addFlashAttribute("pageMessage", "You are signed out.");
        return "redirect:/pasteleria";
    }
}
