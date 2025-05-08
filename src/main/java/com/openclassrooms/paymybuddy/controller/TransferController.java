package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.DTO.ConnectionDTO;
import com.openclassrooms.paymybuddy.DTO.TransactionDTO;
import com.openclassrooms.paymybuddy.DTO.TransferRequestDTO;
import com.openclassrooms.paymybuddy.service.TransferService;
import com.openclassrooms.paymybuddy.utils.RequestResult;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    public String showTransferPage(Model model, Principal principal) {

        if (!model.containsAttribute("transferRequest")) {
            model.addAttribute("transferRequest", new TransferRequestDTO());
        }

        List<ConnectionDTO> connections = transferService.getConnections(principal.getName());
        model.addAttribute("connections", connections);

        int userBalance = transferService.getUserBalance(principal.getName());
        model.addAttribute("userBalance", userBalance);

        List<TransactionDTO> transactions = transferService.getTransactions(principal.getName());
        model.addAttribute("transactions", transactions);

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
            redirectAttributes.addFlashAttribute("errorMessage", result.getMessage());
        }
        return "redirect:/transfer";
    }
}
