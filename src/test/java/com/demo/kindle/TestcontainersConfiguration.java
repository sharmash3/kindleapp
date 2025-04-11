package com.demo.kindle;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
@Testcontainers
public class TestcontainersConfiguration {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:17-alpine"));

    @Container
    static RabbitMQContainer rabbitmq = new RabbitMQContainer(DockerImageName.parse("rabbitmq:4.0.6-alpine"));

    @Container
    static GenericContainer<?> zipkin =
            new GenericContainer<>(DockerImageName.parse("openzipkin/zipkin:3.4.4")).withExposedPorts(9411);

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
        return postgres;
    }

    @Bean
    @ServiceConnection
    RabbitMQContainer rabbitmq() {
        return rabbitmq;
    }

    @Bean
    @ServiceConnection(name = "openzipkin/zipkin")
    GenericContainer<?> zipkinContainer() {
        return zipkin;
    }

    @BeforeAll
    static void startContainers() {
        // Start all containers manually before the tests
        postgres.start();
        rabbitmq.start();
        zipkin.start();
        System.out.println("Testcontainers started and services are running...");
    }

    @AfterAll
    static void stopContainers() {
        // Ensure the containers are stopped after the tests finish
        if (postgres.isRunning()) {
            postgres.stop();
        }
        if (rabbitmq.isRunning()) {
            rabbitmq.stop();
        }
        if (zipkin.isRunning()) {
            zipkin.stop();
        }
        System.out.println("Testcontainers stopped.");
    }
}
