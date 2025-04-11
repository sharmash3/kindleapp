package com.demo.kindle.orders;

import com.demo.kindle.orders.domain.OrderEntity;
import com.demo.kindle.orders.domain.OrderService;
import com.demo.kindle.orders.mappers.OrderMapper;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class OrdersApi {
    private final OrderService orderService;

    public OrdersApi(OrderService orderService) {
        this.orderService = orderService;
    }

    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        OrderEntity orderEntity = OrderMapper.convertToEntity(request);
        var order = orderService.createOrder(orderEntity);
        return new CreateOrderResponse(order.getOrderNumber());
    }

    public Optional<OrderDto> findOrder(String orderNumber) {
        Optional<OrderEntity> byOrderNumber = orderService.findOrder(orderNumber);
        if (byOrderNumber.isEmpty()) {
            return Optional.empty();
        }
        OrderEntity orderEntity = byOrderNumber.get();
        var orderDto = OrderMapper.convertToDto(orderEntity);
        return Optional.of(orderDto);
    }

    public List<OrderView> findOrders() {
        List<OrderEntity> orders = orderService.findOrders();
        return OrderMapper.convertToOrderViews(orders);
    }
}
