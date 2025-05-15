package com.openclassrooms.paymybuddy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for the PayMyBuddy application.
 * This class serves as the entry point for the Spring Boot application.
 */
@SpringBootApplication
public class PaymybuddyApplication {

    /**
     * Main method to launch the PayMyBuddy application.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(PaymybuddyApplication.class, args);
    }

}