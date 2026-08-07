package com.pasteleriaPri.Pasteleria.controller.web;

import com.pasteleriaPri.Pasteleria.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Model attributes every web page needs, so no individual controller has to
 * remember them. Right now that is only the navbar cart badge.
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
        ClientWebController.class
})
public class GlobalWebModelAdvice {

    @Autowired
    private ICartService cartService;

    @ModelAttribute("cartItemCount")
    public int cartItemCount() {
        return cartService.getItemCount();
    }
}
