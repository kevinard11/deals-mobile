package com.okta.example.validation;

import com.okta.examples.adapter.dto.request.RegisterRequest;
import com.okta.examples.service.validation.AuthenticationValidation;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertTrue;

@SpringBootTest
public class AuthenticationValidationTest {

    @Autowired
    AuthenticationValidation authenticationValidation;

    @Test
    public void registerTest(){
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFirst_name("kevin");
        registerRequest.setLast_name("ard");
        registerRequest.setEmail("kevinard11@gmail.com");
        registerRequest.setPassword("P@ssw0rd");
        registerRequest.setConfirmPassword("P@ssw0rd");
        registerRequest.setPhoneNumber("+6281287878787");

        assertTrue(authenticationValidation.register(registerRequest, "/register").getStatusCode().is2xxSuccessful());
    }
}
