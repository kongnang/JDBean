package com.example.jdbean;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JdBeanApplication {

    public static void main(String[] args) {
        SpringApplication.run(JdBeanApplication.class, args);
    }

}
