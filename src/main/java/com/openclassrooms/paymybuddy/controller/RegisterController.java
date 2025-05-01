package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.DTO.RegisterRequestDTO;
import com.openclassrooms.paymybuddy.service.RegisterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    RegisterService registerService;

    @GetMapping
    public String register() {
        return "register";
    }

    @PostMapping (consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> registerNewUser(@ModelAttribute @Valid RegisterRequestDTO registerRequest,
                                               BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid data");
        }
        registerService.register(registerRequest);
        return ResponseEntity.status(201).body("User registered successfully");
    }
}
