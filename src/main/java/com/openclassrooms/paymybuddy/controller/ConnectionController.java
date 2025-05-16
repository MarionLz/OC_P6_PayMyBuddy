package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.service.ConnectionService;
import com.openclassrooms.paymybuddy.utils.RequestResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

/**
 * Controller for managing user connections.
 * Handles requests related to viewing and adding connections.
 */
@Controller
@RequestMapping("/connections")
public class ConnectionController {

    private static final Logger logger = LogManager.getLogger("ConnectionController");

    @Autowired
    private ConnectionService connectionService;

    /**
     * Handles GET requests to the /connections endpoint.
     * Displays the connections page.
     *
     * @return the name of the view to render ("connections").
     */
    @GetMapping
    public String showConnectionsPage() {
        logger.info("GET request to /connections");
        return "connections";
    }

    /**
     * Handles POST requests to the /connections endpoint.
     * Adds a new connection for the current user.
     *
     * @param targetEmail the email of the user to connect with.
     * @param principal the currently authenticated user's principal.
     * @param model the model to pass attributes to the view.
     * @return the name of the view to render ("connections").
     */
    @PostMapping
    public String addConnection(@RequestParam("email") String targetEmail, Principal principal, Model model) {
        logger.info("POST request to /connections");

        String currentUserEmail = principal.getName();

        RequestResult result = connectionService.addConnection(currentUserEmail, targetEmail);
        if (result.isSuccess()) {
            logger.info("Connection added successfully: '{}' → '{}'", currentUserEmail, targetEmail);
            model.addAttribute("successMessage", result.getMessage());
        } else {
            logger.warn("Failed to add connection: '{}' → '{}'. Reason: {}", currentUserEmail, targetEmail, result.getMessage());
            model.addAttribute("errorMessage", result.getMessage());
        }
        return "connections";
    }
}