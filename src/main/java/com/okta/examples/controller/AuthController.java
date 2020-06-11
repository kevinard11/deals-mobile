package com.okta.examples.controller;

import com.okta.examples.adapter.dto.request.ForgotPasswordRequest;
import com.okta.examples.adapter.dto.request.LoginRequest;
import com.okta.examples.adapter.dto.request.RegisterRequest;
import com.okta.examples.service.usecase.AuthenticationService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthController {

    @Autowired
    AuthenticationService authentication;


    @GetMapping("/test")
    public ResponseEntity<?> siginin(){
//        return new ResponseEntity<>(result.getBody(), result.getStatusCode());
        return authentication.test(null);
    }

    @GetMapping("/")
    public ResponseEntity<?> welcome(HttpServletRequest request){
        return new ResponseEntity<>("Welcome. Your session id : "+request.getSession().getId(), HttpStatus.OK);
    }

    @GetMapping("/signout")
    public ResponseEntity<?> signout(HttpServletRequest request){
        request.getSession().invalidate();
        return new ResponseEntity<>("log out", HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest){
        return new ResponseEntity<>(authentication.register(registerRequest), HttpStatus.CREATED);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return new ResponseEntity<>(authentication.login(loginRequest), HttpStatus.OK);
    }

    @PostMapping(value ="/request-otp")
    public ResponseEntity<?> requestOtp(@RequestBody JSONObject data){
        return new ResponseEntity<>(authentication.requestOtp(data), HttpStatus.OK);
    }

    @PostMapping(value ="/{idUser}/match-otp")
    public ResponseEntity<?> matchOtp(@PathVariable("idUser") String idUser, @RequestBody JSONObject data){
        return new ResponseEntity<>(authentication.matchOtp(idUser, data), HttpStatus.OK);
    }

    @PostMapping(value = "/{idUser}/forgot-password")
    public ResponseEntity<?> forgotPassword(@PathVariable("idUser") String idUser,
                                            @RequestBody ForgotPasswordRequest forgotPasswordRequest){
        return new ResponseEntity<>(authentication.forgotPassword(idUser, forgotPasswordRequest), HttpStatus.OK);
    }

}
