package com.pasteleriaPri.Pasteleria.controller;

import com.pasteleriaPri.Pasteleria.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Model attributes every web page needs, so no individual controller has to
 * remember them. Right now that is only the navbar cart badge.
 *
 * Scoped with assignableTypes rather than left global: the @RestControllers in
 * this same package must not have anything injected into their model. New web
 * controllers have to be added to this list.
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