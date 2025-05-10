package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.service.ConnectionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ConnectionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ConnectionService connectionService;

    @InjectMocks
    private ConnectionController connectionController;

    @Test
    void showConnectionsPageShouldReturnConnectionsView() throws Exception {

        mockMvc.perform(get("/connections"))
                .andExpect(status().isOk())
                .andExpect(view().name("connections"));
    }

    @Test
    void addConnectionShouldReturnSuccessMessageWhenConnectionIsSuccessful() throws Exception {

        mockMvc.perform(post("/connections")
                        .param("email", "newconnection@example.com")
                        .principal(() -> "currentuser@example.com"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("successMessage"))
                .andExpect(view().name("connections"));
    }

    @Test
    void addConnectionShouldReturnErrorMessageWhenConnectionFails() throws Exception {

        mockMvc.perform(post("/connections")
                        .param("email", "invalid@example.com")
                        .principal(() -> "currentuser@example.com"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(view().name("connections"));
    }

    @Test
    void addConnectionShouldReturnErrorMessageWhenEmailIsMissing() throws Exception {

        mockMvc.perform(post("/connections")
                        .param("email", "")
                        .principal(() -> "currentuser@example.com"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(view().name("connections"));
    }
}