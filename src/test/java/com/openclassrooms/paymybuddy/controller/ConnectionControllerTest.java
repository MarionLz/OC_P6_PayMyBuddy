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

/**
 * Unit tests for the ConnectionController class.
 * Verifies the behavior of the controller's endpoints and interactions with the service layer.
 */
@ExtendWith(MockitoExtension.class)
public class ConnectionControllerTest {

    /**
     * MockMvc instance for simulating HTTP requests and testing controller endpoints.
     */
    private MockMvc mockMvc;

    /**
     * Mocked ConnectionService for simulating service layer behavior.
     */
    @Mock
    private ConnectionService connectionService;

    /**
     * Mocked Principal for simulating the currently authenticated user.
     */
    @Mock
    private Principal principal;

    /**
     * Injected instance of ConnectionController to be tested.
     */
    @InjectMocks
    private ConnectionController connectionController;

    /**
     * Sets up the test environment before each test.
     * Configures the MockMvc instance with the ConnectionController and a view resolver.
     */
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(connectionController)
                .setViewResolvers(new InternalResourceViewResolver("/templates/", ".html"))
                .build();
    }

    /**
     * Tests that the /connections endpoint returns the "connections" view.
     *
     * @throws Exception if an error occurs during the request
     */
    @Test
    void testShowConnectionsPage_ShouldReturnConnectionsView() throws Exception {
        mockMvc.perform(get("/connections"))
                .andExpect(status().isOk())
                .andExpect(view().name("connections"));
    }

    /**
     * Tests that a successful connection addition displays a success message.
     *
     * @throws Exception if an error occurs during the request
     */
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

    /**
     * Tests that a failed connection addition displays an error message.
     *
     * @throws Exception if an error occurs during the request
     */
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
}