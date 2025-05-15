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

@Controller
@RequestMapping("/register")
public class RegisterController {

    private static final Logger logger = LogManager.getLogger("RegisterController");

    @Autowired
    RegisterService registerService;

    @GetMapping
    public String register(Model model) {

        logger.info("GET request to /register");
        model.addAttribute("registerRequest", new RegisterRequestDTO());
        return "register";
    }

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
