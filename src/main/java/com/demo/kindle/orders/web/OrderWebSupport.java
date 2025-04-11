package com.demo.kindle.orders.web;

import com.demo.kindle.catalog.ProductApi;
import com.demo.kindle.orders.CreateOrderRequest;
import com.demo.kindle.orders.InvalidOrderException;

class OrderWebSupport {

    private final ProductApi productApi;

    protected OrderWebSupport(ProductApi productApi) {
        this.productApi = productApi;
    }

    protected void validate(CreateOrderRequest request) {
        String code = request.item().code();
        var product = productApi
                .getByCode(code)
                .orElseThrow(() -> new InvalidOrderException("Product not found with code: " + code));
        if (product.price().compareTo(request.item().price()) != 0) {
            throw new InvalidOrderException("Product price mismatch");
        }
    }
}
