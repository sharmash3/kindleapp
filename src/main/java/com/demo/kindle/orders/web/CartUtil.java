package com.demo.kindle.orders.web;

import jakarta.servlet.http.HttpSession;

public final class CartUtil {
    public static Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
        }
        return cart;
    }

    private CartUtil() {}
}
