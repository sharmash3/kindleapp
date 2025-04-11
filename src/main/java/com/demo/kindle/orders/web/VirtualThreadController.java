package com.demo.kindle.orders.web;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/virtual-threads")
public class VirtualThreadController {

    // Example method using Virtual Threads for simulating a long-running task
    @GetMapping("/execute")
    public String executeTask() {
        // Create an executor service that uses virtual threads
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        // Simulate a task that runs in a virtual thread
        executor.submit(() -> {
            try {
                System.out.println("Starting virtual thread...");
                // Simulate a long-running task
                Thread.sleep(1000);
                System.out.println("Virtual thread finished!");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        return "Task started on a virtual thread!";
    }
}
