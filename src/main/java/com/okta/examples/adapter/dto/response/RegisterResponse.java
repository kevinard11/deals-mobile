package com.okta.examples.adapter.dto.response;

import com.okta.examples.model.User;

public class RegisterResponse {

    private String message;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
