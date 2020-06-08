package com.okta.examples.service.usecase;

import com.okta.examples.adapter.dto.request.ForgotPasswordRequest;
import com.okta.examples.adapter.dto.request.LoginRequest;
import com.okta.examples.adapter.dto.request.RegisterRequest;
import com.okta.examples.adapter.exception.*;
import com.okta.examples.adapter.wrapper.Parser;
import com.okta.examples.adapter.wrapper.ResponseSuccess;
import com.okta.examples.service.microservice.MemberDomain;
import com.okta.examples.service.validation.AuthenticationValidation;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class AuthenticationService {

    @Autowired
    MemberDomain member;

    @Autowired
    SessionService sessionService;

    @Autowired
    AuthenticationValidation validate;

    public JSONObject register(RegisterRequest registerRequest){

        //Register validation
        System.out.println("Register Validation. " +new Date());
        validate.register(registerRequest);

        //Register validation in member domain
        System.out.println("Register. Send data to member domain : "+ Parser.toJsonString(registerRequest));
        ResponseEntity<?> fromMember = member.register(registerRequest);
        System.out.println("Register. Receive data from member domain :"+ fromMember.getBody().toString());


        JSONObject jsonMember = Parser.parseJSON(fromMember.getBody().toString());
        String message = ""+ jsonMember.get("message");

        //Check response
        if (!fromMember.getStatusCode().is2xxSuccessful()){
           throw new RegisterException(message, fromMember.getStatusCode());
        }

        //Create user
        JSONObject user = (JSONObject) jsonMember.get("data");
        String idUser = ""+user.get("idUser");

        //Create and start session
        System.out.println("Start new session for id : " + idUser);
        String idSession = UUID.randomUUID().toString();
        sessionService.startSession(idUser, idSession);

        //Wrap response
        JSONObject result = new JSONObject();
        result.put("token", idSession);
        result.put("user", user);

        return ResponseSuccess.wrap201(result, "Registration is successful.", "/api/auth/register");
    }

    public JSONObject login(LoginRequest loginRequest){

        //Login validation
        System.out.println("Login Validation. " +new Date());
        validate.login(loginRequest);

        //Login validation in member domain
        System.out.println("Login. Send data to member domain : "+ Parser.toJsonString(loginRequest));
        ResponseEntity<?> fromMember = member.login(loginRequest);
        System.out.println("Login. Receive data from member domain :"+ fromMember.getBody().toString());

        JSONObject jsonMember = Parser.parseJSON(fromMember.getBody().toString());
        String message = ""+ jsonMember.get("message");

        //Check response
        if (!fromMember.getStatusCode().is2xxSuccessful()){
            throw new LoginException(message, fromMember.getStatusCode());
        }

        //Create user
        JSONObject user = (JSONObject) jsonMember.get("user");
        String idUser = ""+user.get("idUser");
        String idSession= UUID.randomUUID().toString();

        //Check user session
        System.out.println("Check if id : "+ idUser+" already have session");
        if(sessionService.checkSession(idUser) != null){
            System.out.println("Session found. Destroy old session for id : "+idUser);
            sessionService.destroySession(idUser);
            System.out.println("Start new session for id : " + idUser);

            //Create and start session
            sessionService.startSession(idUser, idSession);

            //Wrap response
            JSONObject result = new JSONObject();
            result.put("token", idSession);
            result.put("user", user);

            return ResponseSuccess.wrap200(result, "You login in another device.", "/api/auth/login");
        }else {
            System.out.println("Session not found for id : "+ idUser);
            System.out.println("Start new session for id : " + idUser);

            //Create and start session
            sessionService.startSession(idUser, idSession);

            //Wrap response
            JSONObject result = new JSONObject();
            result.put("token", idSession);
            result.put("user", user);

            return ResponseSuccess.wrap200(result, "You are Logged in.", "/api/auth/login");
        }
    }

    public JSONObject requestOtp(JSONObject data){

        //Request otp validation
        validate.requestOtp(data);

        //Request otp validation in member domain
        System.out.println("Request OTP. Send data to member domain : "+ Parser.toJsonString(data));
        ResponseEntity<?> fromMember = member.requestOtp(data);
        System.out.println("Request OTP. Receive data from member domain :"+ fromMember.getBody().toString());

        JSONObject jsonMember = Parser.parseJSON(fromMember.getBody().toString());
        String message = ""+ jsonMember.get("message");

        //Check response
        if (!fromMember.getStatusCode().is2xxSuccessful()){
            throw new RequestOtpException(message, fromMember.getStatusCode());
        }

        JSONObject user = (JSONObject) jsonMember.get("data");

        //Wrap response
        return ResponseSuccess.wrap200(user, "Your OTP has sent to your phone number.",
                "/api/auth/request-otp");
    }

    public JSONObject matchOtp(String idUser, JSONObject data){

        // Match otp validation
        validate.matchOtp(data);

        //Match otp validation in member domain
        System.out.println("Match OTP. Send data to member domain : "+ Parser.toJsonString(data));
        ResponseEntity<?> fromMember = member.matchOtp(idUser, data);
        System.out.println("Match OTP. Receive data from member domain :"+ fromMember.getBody().toString());

        JSONObject jsonMember = Parser.parseJSON(fromMember.getBody().toString());
        String message = ""+ jsonMember.get("message");

        //Check response
        if (!fromMember.getStatusCode().is2xxSuccessful()){
            throw new MatchOtpException(message, fromMember.getStatusCode());
        }

        //Wrap response
        return ResponseSuccess.wrap200(null, "OTP Match.",
                "/api/auth/"+idUser+"/match-otp");
    }

    public JSONObject forgotPassword(String idUser, ForgotPasswordRequest forgotPasswordRequest){

        //Forgot password validation
        validate.forgotPassword(forgotPasswordRequest);

        //Forgot password validation in member domain
        System.out.println("Forgot Password. Send data to member domain : "+ Parser.toJsonString(forgotPasswordRequest));
        ResponseEntity<?> fromMember = member.forgotPassword(idUser, forgotPasswordRequest);
        System.out.println("Forgot Password. Receive data from member domain :"+ fromMember.getBody().toString());

        JSONObject jsonMember = Parser.parseJSON(fromMember.getBody().toString());
        String message = ""+ jsonMember.get("message");

        //Check response
        if (!fromMember.getStatusCode().is2xxSuccessful()){
            throw new ForgotPasswordException(message, fromMember.getStatusCode());
        }

        //Wrap response
        return ResponseSuccess.wrap200(null, "Change Password success.",
                "/api/auth/"+idUser+"/forgot-password");
    }

}
