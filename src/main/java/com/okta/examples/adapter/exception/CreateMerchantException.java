package com.okta.examples.adapter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CreateMerchantException  extends ResponseStatusException {

    public CreateMerchantException(String message, HttpStatus status) {
        super(status, message);
        System.out.println(message);
    }
}

