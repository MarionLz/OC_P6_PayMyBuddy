package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.DTO.ConnectionDTO;
import com.openclassrooms.paymybuddy.DTO.TransactionDTO;
import com.openclassrooms.paymybuddy.DTO.TransferRequestDTO;
import com.openclassrooms.paymybuddy.service.TransferService;
import com.openclassrooms.paymybuddy.utils.RequestResult;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger logger = LogManager.getLogger("TransferController");

    @Autowired
    TransferService transferService;

    @GetMapping
    public String showTransferPage(Model model, Principal principal) {

        logger.info("GET request to /transfer");

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

        logger.info("POST request to /transfer");

        String currentUserEmail = principal.getName();

        if (bindingResult.hasErrors()) {
            logger.warn("Validation errors in transfer request by user '{}': {}", currentUserEmail, bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.transferRequest", bindingResult);
            redirectAttributes.addFlashAttribute("transferRequest", transferRequest);
            return "redirect:/transfer";
        }

        logger.info("Processing transfer from user '{}' to '{}', amount: {}",
                currentUserEmail, transferRequest.getReceiverEmail(), transferRequest.getAmount());

        RequestResult result = transferService.transfer(transferRequest, currentUserEmail);
        if (result.isSuccess()) {
            logger.info("Transfer successful: {}", result.getMessage());
            redirectAttributes.addFlashAttribute("successMessage", result.getMessage());
        } else {
            logger.warn("Transfer failed for user '{}': {}", currentUserEmail, result.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", result.getMessage());
        }
        return "redirect:/transfer";
    }
}
