package com.openclassrooms.paymybuddy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class HomeController {

    @GetMapping({"/", "/home"})
    public String showHomePage(@RequestParam(value = "authError", required = false) String authError,
                               @RequestParam(value = "notFound", required = false) String notFound,
                               Model model) {

        if (authError != null) {
            model.addAttribute("errorMessage", "Vous devez être connecté pour accéder à cette page.");
        } else if (notFound != null) {
            model.addAttribute("errorMessage", "Page introuvable.");
        }
        return "home";
    }
}
