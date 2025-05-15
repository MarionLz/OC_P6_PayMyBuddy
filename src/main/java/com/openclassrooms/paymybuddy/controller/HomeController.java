package com.openclassrooms.paymybuddy.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class HomeController {

    private static final Logger logger = LogManager.getLogger("HomeController");

    @GetMapping({"/", "/home"})
    public String showHomePage(@RequestParam(value = "authError", required = false) String authError,
                               @RequestParam(value = "notFound", required = false) String notFound,
                               Model model) {

        logger.info("GET request to /home");

        if (authError != null) {
            logger.warn("Authentication error parameter detected. User tried to access a protected page without being logged in.");
            model.addAttribute("errorMessage", "Vous devez être connecté pour accéder à cette page.");
        } else if (notFound != null) {
            logger.warn("NotFound parameter detected. Possibly redirected from a non-existent page.");
            model.addAttribute("errorMessage", "Page introuvable.");
        }
        else {
            logger.info("Home page accessed without error parameters.");
        }
        return "home";
    }
}
