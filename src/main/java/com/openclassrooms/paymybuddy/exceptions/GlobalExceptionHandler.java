package com.openclassrooms.paymybuddy.exceptions;

import com.openclassrooms.paymybuddy.DTO.RegisterRequestDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LogManager.getLogger("GlobalExceptionHandler");

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
//
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getFieldErrors().forEach(error ->
//                errors.put(error.getField(), error.getDefaultMessage())
//        );
//        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//    }

//    @ExceptionHandler(UserAlreadyExistsException.class)
//    public String handleEmailAlreadyExistsException(UserAlreadyExistsException ex,
//                                                    @ModelAttribute("registerRequest") RegisterRequestDTO registerRequest,
//                                                    BindingResult bindingResult,
//                                                    Model model) {
//
//        logger.error("User already exists: {}", ex.getMessage());
//        bindingResult.rejectValue("email", "error.email", "Cet email est déjà enregistré.");
//        return "register";
//    }
}
