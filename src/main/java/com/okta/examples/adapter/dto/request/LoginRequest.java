package com.okta.examples.adapter.dto.request;

import com.google.gson.Gson;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class LoginRequest {

    //@NotBlank
    @Size(min = 3, max = 20)
    @Pattern(regexp="^[\\w!#$%&’+/=?`{|}~^-]+(?:\\.[\\w!#$%&’+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$",message="Not valid email pattern")
    private String email;

    private String telephone;

    @NotBlank
    @Size(min = 3, max = 20)
    @Pattern(regexp="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])(?=\\S+$).{8,}$",message="Not valid password pattern")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public String toJsonString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
