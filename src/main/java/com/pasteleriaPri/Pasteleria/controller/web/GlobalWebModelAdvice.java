package com.pasteleriaPri.Pasteleria.controller.web;

import com.pasteleriaPri.Pasteleria.dto.ClientDTO;
import com.pasteleriaPri.Pasteleria.service.ICartService;
import com.pasteleriaPri.Pasteleria.service.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Model attributes every web page needs, so no individual controller has to
 * remember them: the navbar cart badge and who is signed in.
 *
 * Scoped with assignableTypes rather than left global. Living in controller.web
 * is NOT what limits this advice - a @ControllerAdvice applies application-wide
 * regardless of its package, so without this list it would also fire for the
 * @RestControllers in controller.rest and pour view state into JSON responses.
 * New web controllers have to be added to this list.
 */
@ControllerAdvice(assignableTypes = {
        HomeWebController.class,
        ProductWebController.class,
        CartWebController.class,
        ClientWebController.class,
        LoginWebController.class
})
public class GlobalWebModelAdvice {

    @Autowired
    private ICartService cartService;

    @Autowired
    private IClientService clientService;

    @ModelAttribute("cartItemCount")
    public int cartItemCount() {
        return cartService.getItemCount();
    }

    /**
     * Null when nobody is signed in - the header fragment keys its whole
     * signed-in/signed-out state off that. The DTO stored in the session has no
     * password on it (see ClientService.recordLogin), so this is safe to hand to
     * a template.
     */
    @ModelAttribute("loggedClient")
    public ClientDTO loggedClient() {
        return clientService.getLoggedClient();
    }
}