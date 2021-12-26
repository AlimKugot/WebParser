package com.leti.webparser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main class to start Spring Framework Application
 */
@SpringBootApplication
@EnableScheduling
public class Application {

    /**
     * Starts program
     * @param args rudiments
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
