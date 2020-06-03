package com.okta.examples.service;

import com.okta.examples.service.exception.UserAlreadyExists;
import com.okta.examples.adapter.dto.request.RegisterRequest;
import com.okta.examples.adapter.dto.response.RegisterResponse;
import com.okta.examples.service.usecase.UserUsecase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
public class Authenticate {

    @Autowired
    JwtService jwtService;

    @Autowired
    UserUsecase userUsecase;

    public String generateToken(String tokenDetail, String id) {
        return jwtService.generateToken(tokenDetail, id);
    }

    public String authRegis(String tokenDetail, String id) {
        return jwtService.authRegis(tokenDetail, id);
    }

    public boolean matchPassword(String password, String encryptPassword){
        return userUsecase.matchPassword(password, encryptPassword);
    }
    public RegisterResponse register(RegisterRequest registerRequest){
       if (userUsecase.checkEmailandTelephone(registerRequest.getEmail(), registerRequest.getTelephone()) > 0){
           throw new UserAlreadyExists("User already Exists", HttpStatus.BAD_REQUEST);
       }

       registerRequest.setId(userUsecase.genereateId());
       registerRequest.setPassword(userUsecase.encryptPassword(registerRequest.getPassword()));
       userUsecase.createUser(registerRequest);

       RegisterResponse registerResponse = new RegisterResponse();
       registerResponse.setUser(userUsecase.findUserByTelephone(registerRequest.getTelephone()));
       registerResponse.getUser().setPassword(null);
       return registerResponse;
    }

    public void saveToken(String id, String token){

    }
}
