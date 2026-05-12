package com.icaro.api_petshop.infra;


import com.icaro.api_petshop.exceptions.EmailNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmailNotFound.class)
    private ResponseEntity<String> EmailNotFoundHandler(EmailNotFound exception) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("email not found");
    }
}