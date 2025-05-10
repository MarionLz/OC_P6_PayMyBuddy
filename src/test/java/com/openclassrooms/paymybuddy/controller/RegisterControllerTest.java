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

@ExtendWith(MockitoExtension.class)
public class RegisterControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RegisterService registerService;

    @InjectMocks
    private RegisterController registerController;

    @BeforeEach
    public void setup() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(registerController)
                .setValidator(validator)
                .setViewResolvers(new InternalResourceViewResolver("/templates/", ".html"))
                .build();
    }

    @Test
    public void testGetRegisterPage() throws Exception {

        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("registerRequest"));
    }

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
