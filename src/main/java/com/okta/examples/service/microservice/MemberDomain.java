package com.okta.examples.service.microservice;

import com.okta.examples.adapter.dto.request.*;
import com.okta.examples.adapter.template.Template;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    private Template template;

    private final String api = "http://localhost:8080";

    public ResponseEntity<?> register(RegisterRequest registerRequest){
       return template.post(api+"/register1", registerRequest);
    }

    public ResponseEntity<?> login(LoginRequest loginRequest){
        return template.post(api+"/login1", loginRequest);
    }

    public ResponseEntity<?> requestOtp(JSONObject data){
        return template.post(api+"/api/auth/request-otp", data);
    }

    public ResponseEntity<?> matchOtp(String idUser, JSONObject data){
        return template.post(api+"/api/auth/"+idUser+"/match-otp", data);
    }

    public ResponseEntity<?> forgotPassword(String idUser, ForgotPasswordRequest forgotPasswordRequest){
        return template.post(api+"/api/auth/"+idUser+"/forgot-password", forgotPasswordRequest);
    }

    public ResponseEntity<?> getProfile(String idUser){
        return template.get(api+"/api/user/"+idUser);
    }

    public ResponseEntity<?> editProfile(String idUser, EditProfileRequest editProfileRequest){
        return template.put(api+"/api/user/"+idUser, editProfileRequest);
    }


    public ResponseEntity<?> logout(String idUser){
        return template.post(api+"/api/user/"+idUser+"/logout", null);
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
