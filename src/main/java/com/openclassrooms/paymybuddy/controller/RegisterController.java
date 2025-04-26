package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.DTO.RegisterRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    @Autowired
    RegisterService registerService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequestDTO registerRequest) {
        registerService.register(registerRequest);
        return ResponseEntity.status(201).body("User registered successfully");
    }
}
