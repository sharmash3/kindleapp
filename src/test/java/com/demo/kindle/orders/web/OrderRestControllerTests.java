package com.demo.kindle.orders.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.demo.kindle.TestcontainersConfiguration;
import com.demo.kindle.catalog.ProductApi;
import com.demo.kindle.catalog.ProductDto;
import com.demo.kindle.orders.CreateOrderRequest;
import com.demo.kindle.orders.domain.OrderEntity;
import com.demo.kindle.orders.domain.OrderService;
import com.demo.kindle.orders.domain.models.Customer;
import com.demo.kindle.orders.domain.models.OrderCreatedEvent;
import com.demo.kindle.orders.domain.models.OrderItem;
import com.demo.kindle.orders.mappers.OrderMapper;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.AssertablePublishedEvents;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@ApplicationModuleTest(webEnvironment = RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
@AutoConfigureMockMvc
class OrderRestControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderService orderService;

    @MockitoBean
    ProductApi productApi;

    @BeforeEach
    void setUp() {
        ProductDto product = new ProductDto("P100", "The Hunger Games", "", null, new BigDecimal("34.0"));
        given(productApi.getByCode("P100")).willReturn(Optional.of(product));
    }

    @Test
    void shouldCreateOrderSuccessfully(AssertablePublishedEvents events) throws Exception {
        mockMvc.perform(
                        post("/api/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        """
                                                {
                                                    "customer": {
                                                        "name": "Siva",
                                                        "email": "siva123@gmail.com",
                                                        "phone": "9876523456"
                                                   },
                                                    "deliveryAddress": "James, Bangalore, India",
                                                    "item":{
                                                            "code": "P100",
                                                            "name": "The Hunger Games",
                                                            "price": 34.0,
                                                            "quantity": 1
                                                    }
                                                }
                                                """))
                .andExpect(status().isCreated());

        assertThat(events)
                .contains(OrderCreatedEvent.class)
                .matching(e -> e.customer().email(), "siva123@gmail.com")
                .matching(OrderCreatedEvent::productCode, "P100");
    }

    @Test
    void shouldReturnNotFoundWhenOrderIdNotExist() throws Exception {
        mockMvc.perform(get("/api/orders/{orderNumber}", "non-existing-order-id"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldGetOrderSuccessfully() throws Exception {
        OrderEntity orderEntity = buildOrderEntity();
        OrderEntity savedOrder = orderService.createOrder(orderEntity);

        mockMvc.perform(get("/api/orders/{orderNumber}", savedOrder.getOrderNumber()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNumber", is(savedOrder.getOrderNumber())));
    }

    @Test
    void shouldGetOrdersSuccessfully() throws Exception {
        OrderEntity orderEntity = buildOrderEntity();
        orderService.createOrder(orderEntity);

        mockMvc.perform(get("/api/orders")).andExpect(status().isOk());
    }

    private static OrderEntity buildOrderEntity() {
        CreateOrderRequest request = buildCreateOrderRequest();
        return OrderMapper.convertToEntity(request);
    }

    private static CreateOrderRequest buildCreateOrderRequest() {
        OrderItem item = new OrderItem("P100", "The Hunger Games", new BigDecimal("34.0"), 1);
        return new CreateOrderRequest(
                new Customer("Siva", "siva@gmail.com", "77777777"), "Siva, Hyderabad, India", item);
    }
}
