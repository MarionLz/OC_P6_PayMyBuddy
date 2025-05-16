package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.DTO.RegisterRequestDTO;
import com.openclassrooms.paymybuddy.service.RegisterService;
import com.openclassrooms.paymybuddy.exceptions.UserAlreadyExistsException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Unit tests for the RegisterController class.
 * Verifies the behavior of the registration-related endpoints and interactions with the service layer.
 */
@ExtendWith(MockitoExtension.class)
public class RegisterControllerTest {

    /**
     * MockMvc instance for simulating HTTP requests and testing controller endpoints.
     */
    private MockMvc mockMvc;

    /**
     * Mocked RegisterService for simulating service layer behavior.
     */
    @Mock
    private RegisterService registerService;

    /**
     * Injected instance of RegisterController to be tested.
     */
    @InjectMocks
    private RegisterController registerController;

    /**
     * Sets up the test environment before each test.
     * Configures the MockMvc instance with the RegisterController, a validator, and a view resolver.
     */
    @BeforeEach
    public void setup() {

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(registerController)
                .setValidator(validator)
                .setViewResolvers(new InternalResourceViewResolver("/templates/", ".html"))
                .build();
    }

    /**
     * Tests that the GET /register endpoint returns the "register" view
     * and includes a "registerRequest" attribute in the model.
     *
     * @throws Exception if an error occurs during the request
     */
    @Test
    public void testGetRegisterPage() throws Exception {

        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("registerRequest"));
    }

    /**
     * Tests that a successful POST /register request redirects to the login page
     * with a "registered" parameter.
     *
     * @throws Exception if an error occurs during the request
     */
    @Test
    void testPostRequest_ShouldRedirectToLoginOnSuccessfulRegistration() throws Exception {

        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setUsername("alice");
        request.setEmail("alice@example.com");
        request.setPassword("password123");

        mockMvc.perform(post("/register")
                        .param("username", request.getUsername())
                        .param("email", request.getEmail())
                        .param("password", request.getPassword()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?registered"));

        verify(registerService).register(any(RegisterRequestDTO.class));
    }

    /**
     * Tests that a POST /register request with validation errors
     * returns to the "register" view and includes field errors in the model.
     *
     * @throws Exception if an error occurs during the request
     */
    @Test
    void testPostRequest_ShouldReturnToRegisterPageOnValidationError() throws Exception {

        mockMvc.perform(post("/register")
                        .param("username", "")  // Invalid: blank
                        .param("email", "invalid-email")  // Possibly valid format-wise
                        .param("password", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("registrationError"))
                .andExpect(model().attributeHasFieldErrors("registerRequest", "username", "password"));
    }

    /**
     * Tests that a POST /register request with an existing user
     * returns to the "register" view and includes a registration error in the model.
     *
     * @throws Exception if an error occurs during the request
     */
    @Test
    void testPostRequest_ShouldReturnToRegisterPageWhenUserAlreadyExists() throws Exception {

        doThrow(new UserAlreadyExistsException("Email already in use"))
                .when(registerService).register(any(RegisterRequestDTO.class));

        mockMvc.perform(post("/register")
                        .param("username", "bob")
                        .param("email", "bob@example.com")
                        .param("password", "securepass"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("registrationError"))
                .andExpect(model().attributeHasFieldErrors("registerRequest", "email"));
    }

}