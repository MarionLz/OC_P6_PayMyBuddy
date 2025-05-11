package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.service.ConnectionService;
import com.openclassrooms.paymybuddy.utils.RequestResult;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.security.Principal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ConnectionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ConnectionService connectionService;
    @Mock
    private Principal principal;

    @InjectMocks
    private ConnectionController connectionController;

    @BeforeEach
    public void setup() {

        mockMvc = MockMvcBuilders.standaloneSetup(connectionController)
                .setViewResolvers(new InternalResourceViewResolver("/templates/", ".html"))
                .build();
    }

    @Test
    void testShowConnectionsPage_ShouldReturnConnectionsView() throws Exception {

        mockMvc.perform(get("/connections"))
                .andExpect(status().isOk())
                .andExpect(view().name("connections"));
    }

    @Test
    void testAddConnection_ShouldAddSuccessMessageWhenConnectionIsSuccessful() throws Exception {

        when(principal.getName()).thenReturn("currentuser@example.com");
        when(connectionService.addConnection("currentuser@example.com", "newconnection@example.com"))
                .thenReturn(new RequestResult(true, "Connection added successfully"));

        mockMvc.perform(post("/connections")
                        .param("email", "newconnection@example.com")
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("connections"))
                .andExpect(model().attributeExists("successMessage"))
                .andExpect(model().attribute("successMessage", "Connection added successfully"));

        verify(connectionService, times(1)).addConnection("currentuser@example.com", "newconnection@example.com");
    }

    @Test
    void testAddConnection_ShouldAddErrorMessageWhenConnectionFails() throws Exception {

        when(principal.getName()).thenReturn("currentuser@example.com");
        when(connectionService.addConnection("currentuser@example.com", "invalid@example.com"))
                .thenReturn(new RequestResult(false, "Connection failed"));

        mockMvc.perform(post("/connections")
                        .param("email", "invalid@example.com")
                        .principal(principal))
                .andExpect(status().isOk())
                .andExpect(view().name("connections"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", "Connection failed"));

        verify(connectionService, times(1)).addConnection("currentuser@example.com", "invalid@example.com");
    }

//    @Test
//    void testAddConnection_ShouldAddErrorMessageWhenEmailIsMissing() throws Exception {
//
//        when(principal.getName()).thenReturn("currentuser@example.com");
//
//        mockMvc.perform(post("/connections")
//                        .param("email", "")
//                        .principal(principal))
//                .andExpect(status().isOk())
//                .andExpect(view().name("connections"))
//                .andExpect(model().attributeExists("errorMessage"))
//                .andExpect(model().attribute("errorMessage", "Email is required"));
//
//        verify(connectionService, never()).addConnection(anyString(), anyString());
//        verify(model, times(1)).addAttribute("errorMessage", "Email is required");
//    }
}