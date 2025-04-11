package com.demo.kindle.orders;

import com.demo.kindle.orders.domain.models.Customer;
import com.demo.kindle.orders.domain.models.OrderItem;
import com.demo.kindle.orders.domain.models.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderDto(
        String orderNumber,
        OrderItem item,
        Customer customer,
        String deliveryAddress,
        OrderStatus status,
        LocalDateTime createdAt) {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public BigDecimal getTotalAmount() {
        return item.price().multiply(BigDecimal.valueOf(item.quantity()));
    }
}
