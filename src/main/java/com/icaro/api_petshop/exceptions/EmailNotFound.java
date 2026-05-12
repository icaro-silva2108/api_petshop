package com.icaro.api_petshop.exceptions;

public class EmailNotFound extends RuntimeException {

    public EmailNotFound() {
        super("tutor not found to the given email");
    }

    public EmailNotFound(String message) { super(message); }
}