package com.demo.kindle.orders;

import com.demo.kindle.orders.domain.models.Customer;
import com.demo.kindle.orders.domain.models.OrderStatus;

public record OrderView(String orderNumber, OrderStatus status, Customer customer) {}
