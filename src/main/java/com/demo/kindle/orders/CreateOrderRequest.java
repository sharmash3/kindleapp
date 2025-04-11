package com.demo.kindle.orders;

import com.demo.kindle.orders.domain.models.Customer;
import com.demo.kindle.orders.domain.models.OrderItem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record CreateOrderRequest(@Valid Customer customer, @NotEmpty String deliveryAddress, @Valid OrderItem item) {}
