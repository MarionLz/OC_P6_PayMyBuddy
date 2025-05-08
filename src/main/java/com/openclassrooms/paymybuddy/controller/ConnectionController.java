package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.service.ConnectionService;
import com.openclassrooms.paymybuddy.utils.RequestResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/connections")
public class ConnectionController {

    @Autowired
    private ConnectionService connectionService;

    @GetMapping
    public String showConnectionsPage() {
        return "connections";
    }

    @PostMapping
    public String addConnection(@RequestParam("email") String email, Principal principal, Model model) {

        String currentUserEmail = principal.getName();

        RequestResult result = connectionService.addConnection(currentUserEmail, email);
        if (result.isSuccess()) {
            model.addAttribute("successMessage", result.getMessage());
        } else {
            model.addAttribute("errorMessage", result.getMessage());
        }
        return "connections";
    }
}
