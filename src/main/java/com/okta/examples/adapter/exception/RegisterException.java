package com.okta.examples.adapter.exception;

import com.okta.examples.adapter.exception.test.DealsStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RegisterException extends ResponseStatusException {

    public RegisterException(String reason, HttpStatus status) {
        super(status, reason);
        System.out.println(reason);
    }

    public RegisterException(DealsStatus exceptionCode) {
        super(exceptionCode.getStatus(), exceptionCode.getMessage());
        System.out.println(String.format("%03d",exceptionCode.getValue())+". "+exceptionCode.getMessage());
    }
}
