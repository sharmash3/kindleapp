package com.demo.kindle.inventory;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.demo.kindle.TestcontainersConfiguration;
import com.demo.kindle.orders.domain.models.Customer;
import com.demo.kindle.orders.domain.models.OrderCreatedEvent;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.modulith.test.Scenario;

@ApplicationModuleTest(webEnvironment = RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
class InventoryIntegrationTests {

    @Autowired
    private InventoryService inventoryService;

    @Test
    void handleOrderCreatedEvent(Scenario scenario) {
        var customer = new Customer("Siva", "siva@gmail.com", "9987654");
        String productCode = "P114";
        var event = new OrderCreatedEvent(UUID.randomUUID().toString(), productCode, 2, customer);
        scenario.publish(event).andWaitForStateChange(() -> inventoryService.getStockLevel(productCode) == 598);
    }
}
