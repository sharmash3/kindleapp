package com.demo.kindle.orders.web;

import com.demo.kindle.catalog.ProductApi;
import com.demo.kindle.catalog.ProductDto;
import com.demo.kindle.orders.domain.models.Customer;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HtmxRefreshView;
import io.github.wimdeblauwe.htmx.spring.boot.mvc.HxRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.FragmentsRendering;

@Controller
class CartController {
    private static final Logger log = LoggerFactory.getLogger(CartController.class);
    private final ProductApi productApi;

    CartController(ProductApi productApi) {
        this.productApi = productApi;
    }

    @PostMapping("/buy")
    String addProductToCart(@RequestParam String code, HttpSession session) {
        log.info("Adding product code:{} to cart", code);
        Cart cart = CartUtil.getCart(session);
        ProductDto product = productApi.getByCode(code).orElseThrow();
        cart.setItem(new Cart.LineItem(product.code(), product.name(), product.price(), 1));
        session.setAttribute("cart", cart);
        return "redirect:/cart";
    }

    @GetMapping({"/cart"})
    String showCart(Model model, HttpSession session) {
        Cart cart = CartUtil.getCart(session);
        model.addAttribute("cart", cart);
        OrderForm orderForm = new OrderForm(new Customer("", "", ""), "");
        model.addAttribute("orderForm", orderForm);
        return "cart";
    }

    @HxRequest
    @PostMapping("/update-cart")
    View updateCart(@RequestParam String code, @RequestParam int quantity, HttpSession session) {
        log.info("Updating cart code:{}, quantity:{}", code, quantity);
        Cart cart = CartUtil.getCart(session);
        cart.updateItemQuantity(quantity);
        session.setAttribute("cart", cart);
        boolean refresh = cart.getItem() == null;
        if (refresh) {
            return new HtmxRefreshView();
        }
        return FragmentsRendering.with("partials/cart", Map.of("cart", cart)).build();
    }
}
