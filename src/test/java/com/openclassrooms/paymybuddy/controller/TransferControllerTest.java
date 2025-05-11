package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.DTO.ConnectionDTO;
import com.openclassrooms.paymybuddy.DTO.TransactionDTO;
import com.openclassrooms.paymybuddy.DTO.TransferRequestDTO;
import com.openclassrooms.paymybuddy.service.TransferService;
import com.openclassrooms.paymybuddy.utils.RequestResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
public class TransferControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransferService transferService;
    @Mock
    private Principal principal;
    @Mock
    private RedirectAttributes redirectAttributes;
    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private TransferController transferController;

    @BeforeEach
    public void setup() {

        mockMvc = MockMvcBuilders.standaloneSetup(transferController)
                .setViewResolvers(new InternalResourceViewResolver("/templates/", ".html"))
                .build();
    }

    @Test
    void testShowTransferPage_ShouldPopulateModelAndReturnView() throws Exception {

        when(principal.getName()).thenReturn("user@example.com");
        List<ConnectionDTO> mockConnections = List.of(new ConnectionDTO("friend@example.com"));
        List<TransactionDTO> mockTransactions = List.of(new TransactionDTO("Marion", "remboursement", 100));

        when(transferService.getConnections("user@example.com")).thenReturn(mockConnections);
        when(transferService.getUserBalance("user@example.com")).thenReturn(1000);
        when(transferService.getTransactions("user@example.com")).thenReturn(mockTransactions);

        mockMvc.perform(get("/transfer").principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("transfer"))
                .andExpect(model().attributeExists("transferRequest"))
                .andExpect(model().attribute("connections", mockConnections))
                .andExpect(model().attribute("userBalance", 1000))
                .andExpect(model().attribute("transactions", mockTransactions));
    }

    @Test
    void testProcessTransfer_ShouldRedirect() throws Exception {

        mockMvc.perform(post("/transfer")
                        .param("description", "Dinner")
                        .param("connectionEmail", "friend@example.com")
                        .param("amount", "100")
                        .principal(principal))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/transfer"));
    }

    @Test
    void testProcessTransfer_ShouldRedirectWithSuccessMessage() {

        TransferRequestDTO transferRequest = new TransferRequestDTO("Test", "friend@example.com", 100);

        when(principal.getName()).thenReturn("user@example.com");
        when(bindingResult.hasErrors()).thenReturn(false);
        when(transferService.transfer(transferRequest, "user@example.com"))
                .thenReturn(new RequestResult(true, "Transfer completed"));

        String result = transferController.processTransfer(transferRequest, bindingResult, redirectAttributes, principal);

        assertEquals("redirect:/transfer", result);
        verify(redirectAttributes).addFlashAttribute("successMessage", "Transfer completed");
    }

    @Test
    void testProcessTransfer_ShouldRedirectWithErrorMessage() {

        TransferRequestDTO transferRequest = new TransferRequestDTO("Test", "friend@example.com", 100);

        when(principal.getName()).thenReturn("user@example.com");
        when(bindingResult.hasErrors()).thenReturn(false);
        when(transferService.transfer(transferRequest, "user@example.com"))
                .thenReturn(new RequestResult(false, "Transfer failed"));

        String result = transferController.processTransfer(transferRequest, bindingResult, redirectAttributes, principal);

        assertEquals("redirect:/transfer", result);
        verify(redirectAttributes).addFlashAttribute("errorMessage", "Transfer failed");
    }

    @Test
    public void testProcessTransfer_ShouldRedirectWithValidationErrors() {

        TransferRequestDTO transferRequest = new TransferRequestDTO("Test", "friend@example.com", -1);

        when(bindingResult.hasErrors()).thenReturn(true);

        String result = transferController.processTransfer(transferRequest, bindingResult, redirectAttributes, principal);

        assertEquals("redirect:/transfer", result);
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("org.springframework.validation.BindingResult.transferRequest"), eq(bindingResult));
        verify(redirectAttributes, times(1)).addFlashAttribute(eq("transferRequest"), eq(transferRequest));
    }
}
