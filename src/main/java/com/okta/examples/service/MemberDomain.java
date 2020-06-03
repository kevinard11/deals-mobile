package com.okta.examples.service;

import com.okta.examples.adapter.dto.request.LoginRequest;
import com.okta.examples.adapter.dto.request.RegisterRequest;
import com.okta.examples.adapter.dto.request.TokenRequest;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class MemberDomain {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${jwt.id.mobile}")
    private String token;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    private final String api = "http://localhost:8080";

    public ResponseEntity<?> register(RegisterRequest registerRequest){
        ResponseEntity<?> result = null;
        try {
            result= restTemplate.postForEntity(api + "/register1", registerRequest, JSONObject.class);
        }
        catch (HttpClientErrorException e){
            result = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
            System.out.println(e);
        }
        catch (HttpServerErrorException e){
            result = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
            System.out.println(e);
        }
        return result;
    }

    public ResponseEntity<?> login(LoginRequest loginRequest){
        ResponseEntity<?> result = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            //headers.set("Authorization", "Bearer "+accessToken);
            headers.setBearerAuth("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIzY2NjMzU2ZC02ZTk4LTRjOGItODE2OS0yMWE5MzQyNGEyMzU7bnVsbCIsImV4cCI6MTU5MTc2Mjc3MywiaWF0IjoxNTkxMTU3OTczfQ.i7lx8kch76TIq8vCfJln6q8ps55u4JaiHy-QM9p28QVycRf5T53T8GcHucCEWBN71d7wzP4ugC7YVUqMVOCEag");
            HttpEntity<?> entity = new HttpEntity<>(loginRequest,headers);
            System.out.println(entity.getBody());
            result = restTemplate.exchange(api+"/login1", HttpMethod.POST, entity, JSONObject.class);
            //result= restTemplate.postForEntity(api + "/login1", loginRequest, JSONObject.class);
        }
        catch (HttpClientErrorException e){
            if (e.getRawStatusCode() == 401 ){
                System.out.println("hoho");
            }
            result = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
            System.out.println(e);
        }
        catch (HttpServerErrorException e){
            result = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
            System.out.println(e);
        }
        return result;
    }

    public void saveToken (TokenRequest tokenRequest){
        ResponseEntity<?> result = null;
        try {
            restTemplate.postForEntity(api+"/saveToken", tokenRequest, String.class);
        }
        catch (HttpClientErrorException e){
            result = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
            System.out.println(e);
        }
        catch (HttpServerErrorException e){
            result = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
            System.out.println(e);
        }
    }

    public void updateToken (TokenRequest tokenRequest){
        ResponseEntity<?> result = null;
        try {
            result = restTemplate.postForEntity(api+"/updateToken", tokenRequest, String.class);
        }
        catch (HttpClientErrorException e){
            result = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
            System.out.println(e);
        }
        catch (HttpServerErrorException e){
            result = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
            System.out.println(e);
        }
    }

    public ResponseEntity<?> checkToken (){//String idUser, String tokenDetail){
        ResponseEntity<?> result = null;
        try {
            TokenRequest tokenRequest = new TokenRequest();
            //tokenRequest.setId(idUser); tokenRequest.setToken(tokenDetail);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            //headers.set("Authorization", "Bearer "+accessToken);
            headers.setBearerAuth("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI4NTc5NTM2NS02NzM5LTQ4Y2UtOWMwOS1mM2FmM2E1NjEwMGM7ZTkwMDY5MjYtZmQ5Yi00YmU5LThlZTMtNjM1OGFiMmY4MzkyIiwiZXhwIjoxNTkxNzU2MDY0LCJpYXQiOjE1OTExNTEyNjR9.kvcaL7_UIR-XkSmi7aWN6TVjbRaf17psJErSvToHWn2qd78obPYPL16byKzM-iy1C8iT-pSrkjyGPuxPXOk2qQ");
            HttpEntity<?> entity = new HttpEntity<>(tokenRequest,headers);
            System.out.println(entity.getBody());
            result = restTemplate.exchange(api+"/", HttpMethod.GET, entity, String.class);
        }
        catch (HttpClientErrorException e){
            if (e.getRawStatusCode() == 401 ){
                System.out.println("hoho");
            }
            result = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
            System.out.println(e);
        }
        catch (HttpServerErrorException e) {
            result = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
            System.out.println(e);
        }
        return result;
    }

    public ResponseEntity<?> logout(String idUser){
        ResponseEntity<?> result = null;
        try {
            result= restTemplate.postForEntity(api + "/user/"+idUser+"/logout", null, JSONObject.class);
        }
        catch (HttpClientErrorException e){
            result = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
            System.out.println(e);
        }
        catch (HttpServerErrorException e){
            result = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
            System.out.println(e);
        }
        return result;
    }

    public ResponseEntity<?> getProfile(String idUser){
        ResponseEntity<?> result = null;
        try {
            result= restTemplate.getForEntity(api + "/user/"+idUser, JSONObject.class);
        }
        catch (HttpClientErrorException e){
            result = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
            System.out.println(e);
        }
        catch (HttpServerErrorException e){
            result = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
            System.out.println(e);
        }
        return result;
    }

    public ResponseEntity<?> requestOTP(JSONObject data){
        ResponseEntity<?> result = null;
        try {
            result= restTemplate.postForEntity(api + "/request-oto", data, JSONObject.class);
        }
        catch (HttpClientErrorException e){
            result = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
            System.out.println(e);
        }
        catch (HttpServerErrorException e){
            result = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
            System.out.println(e);
        }
        return result;
    }

    public ResponseEntity<?> verifiedOTP(JSONObject data){
        ResponseEntity<?> result = null;
        try {
            result= restTemplate.postForEntity(api + "/verified-oto", data, JSONObject.class);
        }
        catch (HttpClientErrorException e){
            result = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
            System.out.println(e);
        }
        catch (HttpServerErrorException e){
            result = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
            System.out.println(e);
        }
        return result;
    }
}
