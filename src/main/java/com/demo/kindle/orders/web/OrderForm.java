package com.demo.kindle.orders.web;

import com.demo.kindle.orders.domain.models.Customer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record OrderForm(
        @Valid Customer customer, @NotEmpty(message = "Delivery address is required") String deliveryAddress) {}
