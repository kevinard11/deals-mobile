package com.okta.examples.adapter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserAlreadyExists extends ResponseStatusException {

    public UserAlreadyExists(String message, HttpStatus status) {
        super(status,message);
    }

}
