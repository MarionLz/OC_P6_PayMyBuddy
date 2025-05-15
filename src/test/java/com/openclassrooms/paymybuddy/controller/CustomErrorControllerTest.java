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

@ExtendWith(MockitoExtension.class)
public class CustomErrorControllerTest {

    private MockMvc mockMvc;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private CustomErrorController customErrorController;

    @BeforeEach
    public void setup() {

        mockMvc = MockMvcBuilders.standaloneSetup(customErrorController)
                .setViewResolvers(new InternalResourceViewResolver("/templates/", ".html"))
                .build();
    }

    @Test
    void testHandleError_NotFound_ShouldRedirectToHomeWithNotFoundParam() throws Exception {

        mockMvc.perform(get("/error")
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 404))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home?notFound=1"));
    }

    @Test
    void testHandleError_NoStatus_ShouldRedirectToHome() throws Exception {
        mockMvc.perform(get("/error"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));
    }
}
