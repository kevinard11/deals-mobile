package com.okta.examples.adapter.status;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class MatchOtpException extends ResponseStatusException {

    public MatchOtpException(String message, HttpStatus status) {
        super(status, message);
        System.out.println(message);
    }
}
