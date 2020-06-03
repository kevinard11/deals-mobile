package com.okta.examples.adapter.dto.request;

import com.google.gson.Gson;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RegisterRequest {

    private String id;

    @NotBlank
    @Size(min = 3, max = 20)
    //@Email
    @Pattern(regexp="^[\\w!#$%&’+/=?`{|}~^-]+(?:\\.[\\w!#$%&’+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$",message="Not valid email pattern")
    private String email;

    @NotBlank
    @Size(max = 10)
    private String first_name;

    private String last_name;

    @NotBlank
    @Size(min = 3, max = 20)
    @Pattern(regexp="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*])(?=\\S+$).{8,}$",message="Not valid password pattern")
    private String password;

    @NotBlank
    @Size(min = 3, max = 20)
    private String confirmPassword;

    private String telephone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }


    public String toJsonString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
