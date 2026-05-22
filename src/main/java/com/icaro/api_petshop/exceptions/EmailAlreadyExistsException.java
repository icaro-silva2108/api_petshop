package com.icaro.api_petshop.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException() { super ("email already exists"); }

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
