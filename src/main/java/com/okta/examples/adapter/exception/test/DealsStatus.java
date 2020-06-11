package com.okta.examples.adapter.exception.test;

import org.springframework.http.HttpStatus;

public enum DealsStatus {

    FILL_ALL_FORMS(1,  "Please fill in all the forms.", HttpStatus.BAD_REQUEST),
    NAME_INVALID(2, "Name is invalid.", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(3, "Email is invalid.", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_INVALID(4, "Phone Number is invalid", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(5, "Password is invalid", HttpStatus.BAD_REQUEST);

    private final int value;
    private final String message;
    private final HttpStatus status;

    DealsStatus(int value, String message, HttpStatus status) {
        this.value = value;
        this.message = message;
        this.status = status;
    }

    public int getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
