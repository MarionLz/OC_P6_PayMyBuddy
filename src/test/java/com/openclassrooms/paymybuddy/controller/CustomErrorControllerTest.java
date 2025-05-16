package com.openclassrooms.paymybuddy.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for the CustomErrorController class.
 * Verifies the behavior of the error handling logic and redirection.
 */
@ExtendWith(MockitoExtension.class)
public class CustomErrorControllerTest {

    /**
     * MockMvc instance for simulating HTTP requests and testing controller endpoints.
     */
    private MockMvc mockMvc;

    /**
     * Mocked HttpServletRequest for simulating HTTP request attributes.
     */
    @Mock
    private HttpServletRequest request;

    /**
     * Injected instance of CustomErrorController to be tested.
     */
    @InjectMocks
    private CustomErrorController customErrorController;

    /**
     * Sets up the test environment before each test.
     * Configures the MockMvc instance with the CustomErrorController and a view resolver.
     */
    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(customErrorController)
                .setViewResolvers(new InternalResourceViewResolver("/templates/", ".html"))
                .build();
    }

    /**
     * Tests that when a 404 error occurs, the user is redirected to the home page
     * with a "notFound" parameter.
     *
     * @throws Exception if an error occurs during the request
     */
    @Test
    void testHandleError_NotFound_ShouldRedirectToHomeWithNotFoundParam() throws Exception {
        mockMvc.perform(get("/error")
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 404))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home?notFound=1"));
    }

    /**
     * Tests that when no error status is provided, the user is redirected to the home page.
     *
     * @throws Exception if an error occurs during the request
     */
    @Test
    void testHandleError_NoStatus_ShouldRedirectToHome() throws Exception {
        mockMvc.perform(get("/error"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }
}