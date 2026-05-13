package com.icaro.api_petshop.exceptions;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() { super("invalid credentials"); }

    public InvalidCredentialsException(String message) {
        super(message);
    }
}
