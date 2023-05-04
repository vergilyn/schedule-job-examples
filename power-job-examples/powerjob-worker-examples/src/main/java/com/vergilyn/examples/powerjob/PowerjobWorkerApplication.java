package com.vergilyn.examples.powerjob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PowerjobWorkerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(PowerjobWorkerApplication.class, args);
    }
}
