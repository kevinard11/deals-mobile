package com.okta.examples.adapter.dto.request;

public class UserRequest {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserRequest(String id) {
        this.id = id;
    }
}
