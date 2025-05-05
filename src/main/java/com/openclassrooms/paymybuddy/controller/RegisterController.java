package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.DTO.RegisterRequestDTO;
import com.openclassrooms.paymybuddy.exceptions.UserAlreadyExistsException;
import com.openclassrooms.paymybuddy.service.RegisterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    RegisterService registerService;

    @GetMapping
    public String register(Model model) {

        model.addAttribute("registerRequest", new RegisterRequestDTO());
        return "register";
    }

    @PostMapping
    public String registerNewUser(@ModelAttribute("registerRequest") @Valid RegisterRequestDTO registerRequest,
                                  BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            registerService.register(registerRequest);
        } catch (UserAlreadyExistsException e) {
            bindingResult.rejectValue("email", "error.email", "This email is already registered.");
            return "register";
        }

        return "redirect:/login?registered";
    }
}
