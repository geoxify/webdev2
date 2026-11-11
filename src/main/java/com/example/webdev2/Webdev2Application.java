package com.example.webdev2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ShopProperties.class)
public class Webdev2Application {

    public static void main(String[] args) {
        SpringApplication.run(Webdev2Application.class, args);
    }
}
