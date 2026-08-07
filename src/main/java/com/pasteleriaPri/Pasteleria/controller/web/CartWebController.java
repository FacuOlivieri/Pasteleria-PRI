package com.pasteleriaPri.Pasteleria.controller.web;

import com.pasteleriaPri.Pasteleria.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/pasteleria/cart")
public class CartWebController {

    private static final String DEFAULT_RETURN_URL = "/pasteleria/cart";

    @Autowired
    private ICartService cartService;

    @GetMapping
    public String detail(Model model) {
        model.addAttribute("cart", cartService.getCart());
        return "cart/detail";
    }

    @PostMapping("/add")
    public String add(@RequestParam Long productId,
                      @RequestParam(required = false) Integer quantity,
                      @RequestParam(required = false) String returnUrl,
                      RedirectAttributes redirectAttributes) {
        cartService.addProduct(productId, quantity);
        redirectAttributes.addFlashAttribute("cartMessage", "Added to your cart.");
        return "redirect:" + safeReturnUrl(returnUrl);
    }

    @PostMapping("/{productId}/remove")
    public String remove(@PathVariable Long productId, RedirectAttributes redirectAttributes) {
        cartService.removeProduct(productId);
        redirectAttributes.addFlashAttribute("cartMessage", "Item removed from your cart.");
        return "redirect:" + DEFAULT_RETURN_URL;
    }

    @PostMapping("/clear")
    public String clear(RedirectAttributes redirectAttributes) {
        cartService.clear();
        redirectAttributes.addFlashAttribute("cartMessage", "Your cart is empty again.");
        return "redirect:" + DEFAULT_RETURN_URL;
    }

    /**
     * "Add to cart" sends back the page you came from so you can keep browsing.
     * That value arrives from the browser, so it is never trusted as-is: anything
     * that is not a path inside /pasteleria/ falls back to the cart page. Without
     * this check, a crafted form could bounce a visitor to an external site
     * through our own domain (open redirect).
     */
    private String safeReturnUrl(String returnUrl) {
        if (returnUrl == null || !returnUrl.startsWith("/pasteleria") || returnUrl.startsWith("//")) {
            return DEFAULT_RETURN_URL;
        }
        return returnUrl;
    }
}