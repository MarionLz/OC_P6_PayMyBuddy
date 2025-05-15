package com.openclassrooms.paymybuddy.utils;

import com.openclassrooms.paymybuddy.exceptions.UserNotFoundException;
import com.openclassrooms.paymybuddy.model.User;
import com.openclassrooms.paymybuddy.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserUtils {

    private static final Logger logger = LogManager.getLogger(UserUtils.class);

    private UserUtils() {}

    public static User findUserByEmailOrThrow(UserRepository userRepository, String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User '{}' not found", email);
                    return new UserNotFoundException(email);
                });
    }
}
