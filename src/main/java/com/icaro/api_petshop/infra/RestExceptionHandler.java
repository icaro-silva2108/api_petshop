package com.icaro.api_petshop.infra;

import com.icaro.api_petshop.exceptions.EmailNotFound;
import com.icaro.api_petshop.exceptions.InvalidCredentialsException;
import com.icaro.api_petshop.exceptions.InvalidDateException;
import com.icaro.api_petshop.exceptions.UnauthorizedException;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

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

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
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

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(error ->
                        errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("invalid json");
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<String> genericHandler(Exception exception) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("unexpected internal server error");
    }

    @ExceptionHandler(ExpiredJwtException.class)
    private ResponseEntity<String> ExpiredJwtHandler(ExpiredJwtException exception){

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("expired token");
    }

    @ExceptionHandler(JwtException.class)
    private ResponseEntity<String> InvalidJwtHandler(JwtException exception) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid token");
    }

    @ExceptionHandler(AccessDeniedException.class)
    private ResponseEntity<String> AccessDeniedHandler(AccessDeniedException exception) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("access denied");
    }
}