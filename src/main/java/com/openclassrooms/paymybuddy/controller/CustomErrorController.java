package com.openclassrooms.paymybuddy.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Custom error controller to handle application errors.
 * Redirects users to appropriate pages based on the error status code.
 */
@Controller
public class CustomErrorController implements ErrorController {

    private static final Logger logger = LogManager.getLogger("CustomErrorController");

    /**
     * Handles requests to the /error endpoint.
     * Redirects to the home page with a specific query parameter if the error is a 404 (Not Found).
     * For other errors, redirects to the home page without additional parameters.
     *
     * @param request the {@link HttpServletRequest} containing error details.
     * @return a redirection string to the appropriate page.
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {

        logger.info("Request /error received");

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object uri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);

        logger.info("Error path: {}", uri);
        logger.info("Error status code: {}", status);
        logger.info("Error message: {}", message);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "redirect:/home?notFound=1";
            }
        }

        return "redirect:/home";
    }
}