package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.DTO.TransferRequestDTO;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.service.TransferService;
import com.openclassrooms.paymybuddy.utils.RequestResult;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/transfer")
public class TransferController {

    @Autowired
    TransferService transferService;

    @GetMapping
    public String showTransferPage() {
        return "transfer";
    }

    @PostMapping
    public String processTransfer(@ModelAttribute("transferRequest") @Valid TransferRequestDTO transferRequest,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes, Principal principal) {

        String currentUserEmail = principal.getName();

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.transferRequest", bindingResult);
            redirectAttributes.addFlashAttribute("transferRequest", transferRequest);
            return "redirect:/transfer";
        }

        RequestResult result = transferService.transfer(transferRequest, currentUserEmail);
        if (result.isSuccess()) {
            redirectAttributes.addFlashAttribute("successMessage", result.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("errorMessage",    result.getMessage());
        }
        return "redirect:/transfer";
    }
}
