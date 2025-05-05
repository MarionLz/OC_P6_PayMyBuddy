package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.service.ConnectionService;
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
        if (currentUserEmail.equals(email)) {
            model.addAttribute("errorMessage", "Vous ne pouvez pas vous ajouter vous-même.");
            return "connections";
        }
        if (connectionService.addConnection(currentUserEmail, email)) {
            model.addAttribute("successMessage", "Nouvelle relation ajoutée avec succès.");
        } else {
            model.addAttribute("errorMessage", "Erreur : email non trouvé ou déjà dans votre liste.");
        }
        return "connections";
    }
}
