package com.openclassrooms.paymybuddy.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for handling login-related requests.
 * Manages the display of the login page.
 */
@Controller
public class LoginController {

    private static final Logger logger = LogManager.getLogger("LoginController");

    /**
     * Handles GET requests to the /login endpoint.
     * Displays the login page.
     *
     * @return the name of the view to render ("login").
     */
    @GetMapping("/login")
    public String login() {

        logger.info("GET request to /login");
        return "login";
    }
}
