package com.icaro.api_petshop.infra;


import com.icaro.api_petshop.exceptions.EmailNotFound;
import com.icaro.api_petshop.exceptions.InvalidCredentialsException;
import com.icaro.api_petshop.exceptions.InvalidDateException;
import com.icaro.api_petshop.exceptions.UnauthorizedException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EmailNotFound.class)
    private ResponseEntity<String> EmailNotFoundHandler(EmailNotFound exception) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    private ResponseEntity<String> InvalidCredentialHandler(InvalidCredentialsException exception) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    private ResponseEntity<String> UnauthorizedHandler(UnauthorizedException exception) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<String> EntityNotFoundHandler(EntityNotFoundException exception) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(InvalidDateException.class)
    private ResponseEntity<String> InvalidDateHandler(InvalidDateException exception) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    private ResponseEntity<String> IllegalStateHandler(IllegalStateException exception) {

        return ResponseEntity.status(HttpStatus.CONFLICT).body(exception.getMessage());
    }
}