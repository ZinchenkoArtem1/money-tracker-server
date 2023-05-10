package com.zinchenko;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MoneyTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MoneyTrackerApplication.class, args);
    }
}
