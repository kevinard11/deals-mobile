package com.okta.examples.adapter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class LoginException extends ResponseStatusException {

    public LoginException(String message, HttpStatus status) {
        super(status,message);
        System.out.println(message);
    }
}
