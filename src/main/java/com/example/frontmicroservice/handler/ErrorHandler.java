package com.example.frontmicroservice.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ErrorHandler {


    @ExceptionHandler(ConstraintViolationException.class)
    public String handleValidationException(ConstraintViolationException ex, RedirectAttributes redirectAttributes) {

        List<String> errorMessages = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        redirectAttributes.addFlashAttribute("errors", errorMessages);

        return "redirect:http://localhost:8765/account/registration";
    }
    @ExceptionHandler(JsonProcessingException.class)
    public void handleTimeoutException(JsonProcessingException e) {
        log.error(e.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public void handleUnexpectedException(Exception e) {
        log.error(e.getMessage());
    }

}
