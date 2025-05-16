package com.openclassrooms.paymybuddy.configuration;

import com.openclassrooms.paymybuddy.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration class for the application.
 * Configures authentication, authorization, and security filters.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private LoginService loginService;

    /**
     * Bean definition for the password encoder.
     * Uses BCrypt for encoding passwords.
     *
     * @return a {@link BCryptPasswordEncoder} instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the security filter chain for the application.
     * Defines access rules, exception handling, login, and logout behavior.
     *
     * @param http the {@link HttpSecurity} object to configure.
     * @return the configured {@link SecurityFilterChain}.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for testing with Postman
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/images/**", "/favicon.ico").permitAll() // Allow public access to images
                        .requestMatchers("/", "/home", "/register", "/login", "/error").permitAll() // Allow public access to these endpoints
                        .anyRequest().authenticated() // Require authentication for all other requests
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            String uri = request.getRequestURI();
                            if (uri.equals("/profile") || uri.equals("/transfer") || uri.equals("/connections")) {
                                response.sendRedirect("/home?authError=1"); // Redirect to home with an error parameter
                            } else {
                                response.sendError(HttpServletResponse.SC_NOT_FOUND); // Return 404 for other cases
                            }
                        })
                )
                .formLogin(form -> form
                        .loginPage("/login") // Custom login page
                        .usernameParameter("email") // Use "email" as the username parameter
                        .defaultSuccessUrl("/profile", true) // Redirect to profile on successful login
                        .failureUrl("/login?error") // Redirect to login page with error on failure
                        .permitAll() // Allow public access to the login page
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/home") // Redirect to home on logout
                        .permitAll() // Allow public access to the logout endpoint
                )
                .build();
    }

    /**
     * Bean definition for the authentication manager.
     * Retrieves the authentication manager from the configuration.
     *
     * @param config the {@link AuthenticationConfiguration} object.
     * @return the {@link AuthenticationManager}.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}