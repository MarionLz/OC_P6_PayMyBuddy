package com.openclassrooms.paymybuddy.utils;

import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Utility class for user-related operations.
 * Provides helper methods for managing and retrieving user data.
 */
public class UserUtils {

    private static final Logger logger = LogManager.getLogger(UserUtils.class);

    private UserUtils() {}

    /**
     * Finds a user by their email address or throws an exception if the user is not found.
     *
     * @param userRepository the repository used to search for the user
     * @param email the email address of the user to find
     * @return the User object corresponding to the given email
     * @throws UsernameNotFoundException if no user is found with the given email
     */
    public static User findUserByEmailOrThrow(UserRepository userRepository, String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User '{}' not found", email);
                    return new UsernameNotFoundException(email);
                });
    }
}