package com.demo.kindle;

import org.springframework.boot.SpringApplication;

public class TestBookStoreApplication {

    public static void main(String[] args) {
        SpringApplication.from(BookStoreApplication::main)
                .with(TestcontainersConfiguration.class)
                .run(args);
    }
}
