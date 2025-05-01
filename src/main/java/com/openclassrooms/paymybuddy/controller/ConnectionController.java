package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/connections")
public class ConnectionController {

    @Autowired
    private ConnectionService connectionService;

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<String> addConnection(@RequestParam("email") String email, Principal principal) {

        String currentUserEmail = principal.getName();
        if (currentUserEmail.equals(email)) {
            return ResponseEntity.badRequest().body("You cannot add yourself as a connection.");
        }
        if (connectionService.addConnection(currentUserEmail, email)) {
            return ResponseEntity.ok("Connection added successfully.");
        } else {
            return ResponseEntity.badRequest().body("Failed to add connection. Email not found or already in your list.");
        }
    }
}
