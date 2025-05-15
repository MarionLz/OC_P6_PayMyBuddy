package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class ProfileController {

    private static final Logger logger = LogManager.getLogger("ProfileController");

    @Autowired
    UserRepository userRepository;

    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {

        logger.info("GET request to /profile");

        String email = principal.getName();

        User user = userRepository.findByEmail(email)
                 .orElseThrow(() -> {
                     logger.error("User with email '{}' not found in database.", email);
                     return new UsernameNotFoundException("User not found");
                 });

        logger.info("User '{}' profile loaded successfully.", email);

        model.addAttribute("user", user);
        return "profile";
    }
}
