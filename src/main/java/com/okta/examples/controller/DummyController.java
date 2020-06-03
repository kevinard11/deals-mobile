package com.okta.examples.controller;

import com.okta.examples.adapter.dto.request.LoginRequest;
import com.okta.examples.adapter.dto.request.RegisterRequest;
import com.okta.examples.adapter.dto.request.TokenRequest;
import com.okta.examples.adapter.dto.response.LoginResponse;
import com.okta.examples.adapter.dto.response.RegisterResponse;
import com.okta.examples.service.exception.UserAlreadyExists;
import com.okta.examples.model.Voucher;
import com.okta.examples.service.Authenticate;
import com.okta.examples.service.usecase.UserUsecase;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@Controller
public class DummyController {

    @Autowired
    Authenticate auth;

    @Autowired
    UserUsecase userUsecase;

    @PostMapping(value = "/register1")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest){
        RegisterResponse registerResponse = new RegisterResponse();
        JSONObject jsonObject = new JSONObject();
        if (userUsecase.checkEmailandTelephone(registerRequest.getEmail(), registerRequest.getTelephone()) > 0){
            throw new UserAlreadyExists("User Already Exists", HttpStatus.BAD_REQUEST);
        }
        registerRequest.setId(userUsecase.genereateId());
        registerRequest.setPassword(userUsecase.encryptPassword(registerRequest.getPassword()));
        userUsecase.createUser(registerRequest);

        registerResponse.setUser(userUsecase.findUserByTelephone(registerRequest.getTelephone()));
        registerResponse.getUser().setPassword(null);
        return new ResponseEntity<>(registerResponse, HttpStatus.OK);

    }

    @GetMapping(value = "/voucher")
    public ResponseEntity<?> voucher(){
        JSONObject jsonObject = new JSONObject();
        Voucher voucher = new Voucher();
        return new ResponseEntity<>(voucher, HttpStatus.OK);
    }

    @PostMapping(value = "/login1")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest){
        LoginResponse loginResponse = new LoginResponse();
        JSONObject jsonObject = new JSONObject();
        loginResponse.setUser(userUsecase.findUserByTelephone(loginRequest.getTelephone()));
        if (loginResponse.getUser() == null){
            loginResponse.setMessage("Telephone not found");
            return new ResponseEntity<>(loginResponse, HttpStatus.BAD_REQUEST);
        }
        if (!auth.matchPassword(loginRequest.getPassword(), loginResponse.getUser().getPassword())){
            loginResponse.setMessage("Password wrong");
            loginResponse.setUser(null);
            jsonObject.put("data", loginResponse);
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        }
        if (loginResponse.getUser().getIs_active() != 0){
            loginResponse.setMessage("User already login");
            loginResponse.setUser(null);
            jsonObject.put("data", loginResponse);
            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        }
        loginResponse.getUser().setPassword(null);
        jsonObject.put("data", loginResponse);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

    @PostMapping(value = "/saveToken")
    public ResponseEntity<?> saveToken(@Valid @RequestBody TokenRequest tokenRequest){
        userUsecase.saveToken(tokenRequest.getId(), tokenRequest.getToken());
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @PostMapping(value = "/updateToken")
    public ResponseEntity<?> updateToken(@Valid @RequestBody TokenRequest tokenRequest){
        System.out.println(tokenRequest.getId());
        userUsecase.updateToken(tokenRequest.getId(), tokenRequest.getToken());
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }
}
