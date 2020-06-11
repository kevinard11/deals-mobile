package com.okta.examples.adapter.status;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EditProfileException extends ResponseStatusException {

    public EditProfileException(String message, HttpStatus statuss) {
        super(statuss,message);
        System.out.println(message);
    }
}
