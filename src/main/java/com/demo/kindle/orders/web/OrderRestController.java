package com.demo.kindle.orders.web;

import com.demo.kindle.catalog.ProductApi;
import com.demo.kindle.orders.CreateOrderRequest;
import com.demo.kindle.orders.CreateOrderResponse;
import com.demo.kindle.orders.OrderDto;
import com.demo.kindle.orders.OrderNotFoundException;
import com.demo.kindle.orders.OrderView;
import com.demo.kindle.orders.domain.OrderEntity;
import com.demo.kindle.orders.domain.OrderService;
import com.demo.kindle.orders.mappers.OrderMapper;
import jakarta.validation.Valid;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
class OrderRestController extends OrderWebSupport {
    private static final Logger log = LoggerFactory.getLogger(OrderRestController.class);

    private final OrderService orderService;

    OrderRestController(OrderService orderService, ProductApi productApi) {
        super(productApi);
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateOrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
        validate(request);
        OrderEntity newOrder = OrderMapper.convertToEntity(request);
        var savedOrder = orderService.createOrder(newOrder);
        return new CreateOrderResponse(savedOrder.getOrderNumber());
    }

    @GetMapping(value = "/{orderNumber}")
    OrderDto getOrder(@PathVariable String orderNumber) {
        log.info("Fetching order by orderNumber: {}", orderNumber);
        return orderService
                .findOrder(orderNumber)
                .map(OrderMapper::convertToDto)
                .orElseThrow(() -> OrderNotFoundException.forOrderNumber(orderNumber));
    }

    @GetMapping
    List<OrderView> getOrders() {
        List<OrderEntity> orders = orderService.findOrders();
        return OrderMapper.convertToOrderViews(orders);
    }
}
