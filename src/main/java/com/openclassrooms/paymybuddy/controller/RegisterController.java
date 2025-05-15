package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.DTO.RegisterRequestDTO;
import com.openclassrooms.paymybuddy.exceptions.UserAlreadyExistsException;
import com.openclassrooms.paymybuddy.service.RegisterService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling user registration requests.
 * Manages the display of the registration page and the registration process for new users.
 */
@Controller
@RequestMapping("/register")
public class RegisterController {

    private static final Logger logger = LogManager.getLogger("RegisterController");

    @Autowired
    RegisterService registerService;

    /**
     * Handles GET requests to the /register endpoint.
     * Displays the registration page with an empty registration form.
     *
     * @param model the {@link Model} object used to pass attributes to the view.
     * @return the name of the view to render ("register").
     */
    @GetMapping
    public String register(Model model) {

        logger.info("GET request to /register");
        model.addAttribute("registerRequest", new RegisterRequestDTO());
        return "register";
    }

    /**
     * Handles POST requests to the /register endpoint.
     * Processes the registration form submitted by the user.
     * Validates the input data and attempts to register a new user.
     * If validation errors or a user already exists, returns the registration page with error messages.
     * On successful registration, redirects to the login page with a success message.
     *
     * @param registerRequest the {@link RegisterRequestDTO} containing the user's registration data.
     * @param bindingResult the {@link BindingResult} object containing validation results.
     * @param model the {@link Model} object used to pass attributes to the view.
     * @return the name of the view to render or a redirection string.
     */
    @PostMapping
    public String registerNewUser(@ModelAttribute("registerRequest") @Valid RegisterRequestDTO registerRequest,
                                  BindingResult bindingResult, Model model) {

        logger.info("POST request to /register with data: {}", registerRequest);

        if (bindingResult.hasErrors()) {
            logger.error("Validation errors occurred: {}", bindingResult.getAllErrors());
            model.addAttribute("registrationError", "Un problème est survenu lors de votre inscription.");
            return "register";
        }

        try {
            registerService.register(registerRequest);
        } catch (UserAlreadyExistsException ex) {
            bindingResult.rejectValue("email", "error.email", "Cet email est déjà enregistré.");
            model.addAttribute("registrationError", "Un problème est survenu lors de votre inscription.");
            logger.error("User already exists: {}", ex.getMessage());
            return "register";
        }
        logger.info("User registered successfully: {}", registerRequest.getEmail());
        return "redirect:/login?registered";
    }
}
