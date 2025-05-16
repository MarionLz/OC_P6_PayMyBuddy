package com.openclassrooms.paymybuddy.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the LoginController class.
 * Verifies the behavior of the login endpoint and its interactions with the view layer.
 */
public class LoginControllerTest {

    /**
     * MockMvc instance for simulating HTTP requests and testing controller endpoints.
     */
    private MockMvc mockMvc;

    /**
     * Instance of LoginController to be tested.
     */
    private LoginController loginController;

    /**
     * Sets up the test environment before each test.
     * Configures the MockMvc instance with the LoginController and a view resolver.
     */
    @BeforeEach
    public void setup() {
        loginController = new LoginController();
        mockMvc = MockMvcBuilders.standaloneSetup(loginController)
                .setViewResolvers(new InternalResourceViewResolver("/templates/", ".html"))
                .build();
    }

    /**
     * Tests that the /login endpoint returns the "login" view.
     *
     * @throws Exception if an error occurs during the request
     */
    @Test
    void loginEndpointShouldReturnLoginView() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }
}