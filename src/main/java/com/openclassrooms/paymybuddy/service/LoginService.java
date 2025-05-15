package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import com.openclassrooms.paymybuddy.utils.UserUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Service class for managing user authentication and loading user details.
 * Implements the UserDetailsService interface to integrate with Spring Security.
 */
@Service
public class LoginService implements UserDetailsService {

    private static final Logger logger = LogManager.getLogger("HomeController");

    @Autowired
    private UserRepository userRepository;

    /**
     * Loads a user by their email address.
     * Retrieves the user from the database and converts it into a UserDetails object for Spring Security.
     *
     * @param email the email of the user to load
     * @return a UserDetails object containing the user's information
     * @throws UsernameNotFoundException if no user is found with the given email
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        logger.info("Attempting to load user with email: {}", email);
        User user = UserUtils.findUserByEmailOrThrow(userRepository, email);
        logger.info("User with email '{}' successfully loaded.", email);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}