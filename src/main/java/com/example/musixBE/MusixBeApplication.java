package com.example.musixBE;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableAutoConfiguration
@EnableWebMvc
@Configuration
@ComponentScan
@SpringBootApplication
public class MusixBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(MusixBeApplication.class, args);
    }

}
