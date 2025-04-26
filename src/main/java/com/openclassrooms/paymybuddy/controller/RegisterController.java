package com.openclassrooms.paymybuddy.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

     @PostMapping("/register")
     public ResponseEntity<?> registerUser(@RequestBody User user) {
         // Validate user input
         // Save user to the database
         return ResponseEntity.ok("User registered successfully");
     }
}
