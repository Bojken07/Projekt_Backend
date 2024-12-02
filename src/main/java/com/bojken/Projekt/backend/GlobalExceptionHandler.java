package com.bojken.Projekt.backend;

import com.bojken.Projekt.backend.model.UserDTO;
import org.springframework.ui.Model;
import com.bojken.Projekt.backend.authorities.UserRole;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // Return 400 status for validation errors
    public String handleValidationError(MethodArgumentNotValidException ex, Model model) {
        BindingResult bindingResult = ex.getBindingResult();

        // Add errors to model for display in the form
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("user", new UserDTO("", "", null));
        model.addAttribute("status", "Form submission failed. Please correct the errors.");

        return "register"; // Redirect back to the register form with error messages
    }
}
