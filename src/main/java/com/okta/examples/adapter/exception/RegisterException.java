package com.okta.examples.adapter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RegisterException extends ResponseStatusException {


    public RegisterException(String message, HttpStatus status) {
        super(status,message);
        System.out.println(message);
    }
}
