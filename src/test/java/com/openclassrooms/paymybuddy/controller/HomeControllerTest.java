package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.service.ConnectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for the HomeController class.
 * Verifies the behavior of the controller's endpoints and interactions with the view layer.
 */
@ExtendWith(MockitoExtension.class)
public class HomeControllerTest {

    /**
     * MockMvc instance for simulating HTTP requests and testing controller endpoints.
     */
    private MockMvc mockMvc;

    /**
     * Instance of HomeController to be tested.
     */
    private HomeController homeController;

    /**
     * Sets up the test environment before each test.
     * Configures the MockMvc instance with the HomeController and a view resolver.
     */
    @BeforeEach
    public void setup() {
        homeController = new HomeController();
        mockMvc = MockMvcBuilders.standaloneSetup(homeController)
                .setViewResolvers(new InternalResourceViewResolver("/templates/", ".html"))
                .build();
    }

    /**
     * Tests that the root endpoint ("/") returns the "home" view.
     *
     * @throws Exception if an error occurs during the request
     */
    @Test
    void testShowHomePage_ShouldReturnHomeView() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    /**
     * Tests that the /home endpoint with an "authError" parameter adds an authentication error message to the model.
     *
     * @throws Exception if an error occurs during the request
     */
    @Test
    void testShowHomePage_WithAuthError_ShouldAddAuthErrorMessage() throws Exception {
        mockMvc.perform(get("/home").param("authError", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", "Vous devez être connecté pour accéder à cette page."));
    }

    /**
     * Tests that the /home endpoint with a "notFound" parameter adds a "page not found" error message to the model.
     *
     * @throws Exception if an error occurs during the request
     */
    @Test
    void testShowHomePage_WithNotFound_ShouldAddNotFoundMessage() throws Exception {
        mockMvc.perform(get("/home").param("notFound", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", "Page introuvable."));
    }
}