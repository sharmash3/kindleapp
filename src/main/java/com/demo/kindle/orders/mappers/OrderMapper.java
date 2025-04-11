package com.demo.kindle.orders.mappers;

import com.demo.kindle.orders.CreateOrderRequest;
import com.demo.kindle.orders.OrderDto;
import com.demo.kindle.orders.OrderView;
import com.demo.kindle.orders.domain.OrderEntity;
import com.demo.kindle.orders.domain.models.OrderStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class OrderMapper {
    private OrderMapper() {}

    public static OrderEntity convertToEntity(CreateOrderRequest request) {
        OrderEntity entity = new OrderEntity();
        entity.setOrderNumber(UUID.randomUUID().toString());
        entity.setStatus(OrderStatus.NEW);
        entity.setCustomer(request.customer());
        entity.setDeliveryAddress(request.deliveryAddress());
        entity.setOrderItem(request.item());
        return entity;
    }

    public static OrderDto convertToDto(OrderEntity order) {
        return new OrderDto(
                order.getOrderNumber(),
                order.getOrderItem(),
                order.getCustomer(),
                order.getDeliveryAddress(),
                order.getStatus(),
                order.getCreatedAt());
    }

    public static List<OrderView> convertToOrderViews(List<OrderEntity> orders) {
        List<OrderView> orderViews = new ArrayList<>();
        for (OrderEntity order : orders) {
            var orderView = new OrderView(order.getOrderNumber(), order.getStatus(), order.getCustomer());
            orderViews.add(orderView);
        }
        return orderViews;
    }
}
