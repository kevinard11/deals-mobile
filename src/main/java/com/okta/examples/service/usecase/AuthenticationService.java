package com.okta.examples.service.usecase;

import com.okta.examples.adapter.jwt.JwtTokenUtil;
import com.okta.examples.adapter.jwt.JwtUserDetailsService;
import com.okta.examples.model.status.DealsStatus;
import com.okta.examples.adapter.parser.Parser;
import com.okta.examples.model.response.ResponseFailed;
import com.okta.examples.model.response.ResponseSuccess;
import com.okta.examples.model.request.ForgotPasswordRequest;
import com.okta.examples.model.request.LoginRequest;
import com.okta.examples.model.request.RegisterRequest;
import com.okta.examples.service.microservice.MemberDomain;
import com.okta.examples.service.validation.AuthenticationValidation;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Service
public class AuthenticationService {

    @Autowired
    MemberDomain member;

    @Autowired
    SessionService sessionService;

    @Autowired
    AuthenticationValidation validate;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    JwtUserDetailsService jwtUserDetailsService;

    public ResponseEntity<JSONObject> register(RegisterRequest registerRequest, String path) {

        //Register validation
        System.out.println("Register Validation. " + Parser.toJsonString(registerRequest));
        ResponseEntity<JSONObject> check = validate.register(registerRequest, path);

        if (!check.getStatusCode().is2xxSuccessful()){
            return check;
        }

        //Register validation in member domain
        System.out.println("Register. Send data to member domain : "+ Parser.toJsonString(registerRequest));
        ResponseEntity<?> fromMember = member.register(registerRequest, path);
        System.out.println("Register. Receive data from member domain :"+ fromMember.getBody().toString());

        JSONObject jsonMember = Parser.parseJSON(fromMember.getBody().toString());
        String message = ""+ jsonMember.get("message");
        String status = ""+jsonMember.get("status");

        //Check response
        if (!fromMember.getStatusCode().is2xxSuccessful()){
            if (fromMember.getBody().toString().toLowerCase().contains("connection refused")){
                return ResponseFailed.wrapResponse(DealsStatus.REQUEST_TIME_OUT, path);
            }
           return ResponseFailed.wrapResponseFailed(message, status, fromMember.getStatusCode(), path);
        }

        //Create user
        JSONObject user = (JSONObject) jsonMember.get("data");

        //Wrap response
        JSONObject result = new JSONObject();
        result.put("user", user);

        return ResponseSuccess.wrapResponse(result, DealsStatus.REGISTRATION_SUCCESS, path);
    }

    public ResponseEntity<JSONObject> login(LoginRequest loginRequest, String path, String request){

        //Login validation
        System.out.println("Login Validation. " +Parser.toJsonString(loginRequest));
        ResponseEntity<JSONObject> check = validate.login(loginRequest, path);

        if (!check.getStatusCode().is2xxSuccessful()){
            return check;
        }

        //Login validation in member domain
        System.out.println("Login. Send data to member domain : "+ Parser.toJsonString(loginRequest));
        ResponseEntity<?> fromMember = member.login(loginRequest);
        System.out.println("Login. Receive data from member domain :"+ fromMember.getBody().toString());

        JSONObject jsonMember = Parser.parseJSON(fromMember.getBody().toString());
        String message = ""+ jsonMember.get("message");
        String status = ""+jsonMember.get("status");

        //Check response
        if (!fromMember.getStatusCode().is2xxSuccessful()){
            if (fromMember.getBody().toString().toLowerCase().contains("connection refused")){
                return ResponseFailed.wrapResponse(DealsStatus.REQUEST_TIME_OUT, path);
            }
            return ResponseFailed.wrapResponseFailed(message, status, fromMember.getStatusCode(), path);
        }

        //Create user
        JSONObject user = (JSONObject) jsonMember.get("data");
        String idUser = ""+user.get("idUser");
//        String balance = ""+user.get("balance");
//        user.put("balance", balance);
        String idSession= UUID.randomUUID().toString()+idUser;
        //Check user session
        System.out.println("Check if id : "+ idUser+" already have session");
        if(sessionService.checkSession(idUser) != null){

            System.out.println("Session found. Destroy old session for id : "+idUser);
            sessionService.destroySession(idUser);
            System.out.println("Start new session for id : " + idUser);

            //Create and start session
            sessionService.startSession(idUser, idSession);
            idSession = jwtTokenUtil.generateToken(jwtUserDetailsService.loadUserByUsername(idSession));

            //Wrap response
            JSONObject result = new JSONObject();
            result.put("token", idSession);
            result.put("user", user);

            return ResponseSuccess.wrapResponse(result, DealsStatus.LOGIN_ANOTHER_DEVICE, path);
        }else {

            System.out.println("Session not found for id : "+ idUser);
            System.out.println("Start new session for id : " + idUser);

            //Create and start session
            sessionService.startSession(idUser, idSession);
            idSession = jwtTokenUtil.generateToken(jwtUserDetailsService.loadUserByUsername(idSession));

            //Wrap response
            JSONObject result = new JSONObject();
            result.put("token", idSession);
            result.put("user", user);

            return ResponseSuccess.wrapResponse(result, DealsStatus.LOGIN_SUCCESS, path);
        }
    }

    public ResponseEntity<JSONObject> requestOtp(JSONObject data, String path){

        //Request otp validation
        System.out.println("Request Otp Validation. " +Parser.toJsonString(data));
        ResponseEntity<JSONObject> check = validate.requestOtp(data, path);

        if (!check.getStatusCode().is2xxSuccessful()){
            return check;
        }

        String phoneNumber = (""+data.get("phoneNumber"));
        data.put("phoneNumber", phoneNumber);

        //Request otp validation in member domain
        System.out.println("Request OTP. Send data to member domain : "+ Parser.toJsonString(data));
        ResponseEntity<?> fromMember = member.requestOtp(data);
        System.out.println("Request OTP. Receive data from member domain :"+ fromMember.getBody().toString());

        JSONObject jsonMember = Parser.parseJSON(fromMember.getBody().toString());
        String message = ""+ jsonMember.get("message");
        String status = ""+jsonMember.get("status");

        //Check response
        if (!fromMember.getStatusCode().is2xxSuccessful()){
            if (fromMember.getBody().toString().toLowerCase().contains("connection refused")){
                return ResponseFailed.wrapResponse(DealsStatus.REQUEST_TIME_OUT, path);
            }
            return ResponseFailed.wrapResponseFailed(message, status, fromMember.getStatusCode(), path);
        }

        JSONObject user = (JSONObject) jsonMember.get("data");
        String idUser = ""+user.get("idUser");

        if (sessionService.checkSession(idUser) != null){
            if (sessionService.checkSessionExpiredWithoutSession(idUser) > 0){
                sessionService.destroySession(idUser);
            }else {
                return ResponseFailed.wrapResponse(DealsStatus.ALREADY_LOGIN, path);
            }
        }

        //Wrap response
        return ResponseSuccess.wrapResponse(user, DealsStatus.REQUEST_OTP, path);
    }

    public ResponseEntity<JSONObject> matchOtp(String idUser, JSONObject data, String path){

        // Match otp validation
        System.out.println("Match Otp Validation. " +Parser.toJsonString(data));
        ResponseEntity<JSONObject> check = validate.matchOtp(data, path);

        if (!check.getStatusCode().is2xxSuccessful()){
            return check;
        }

        if (sessionService.checkSession(idUser) != null){
            if (sessionService.checkSessionExpiredWithoutSession(idUser) > 0){
                sessionService.destroySession(idUser);
            }else {
                return ResponseFailed.wrapResponse(DealsStatus.ALREADY_LOGIN, path);
            }
        }
        //Match otp validation in member domain
        System.out.println("Match OTP. Send data to member domain : "+ Parser.toJsonString(data));
        ResponseEntity<?> fromMember = member.matchOtp(idUser, data);
        System.out.println("Match OTP. Receive data from member domain :"+ fromMember.getBody().toString());

        JSONObject jsonMember = Parser.parseJSON(fromMember.getBody().toString());
        String message = ""+ jsonMember.get("message");
        String status = ""+jsonMember.get("status");

        //Check response
        if (!fromMember.getStatusCode().is2xxSuccessful()){
            if (fromMember.getBody().toString().toLowerCase().contains("connection refused")){
                return ResponseFailed.wrapResponse(DealsStatus.REQUEST_TIME_OUT, path);
            }
            return ResponseFailed.wrapResponseFailed(message, status, fromMember.getStatusCode(), path);
        }

        //Wrap response
        return ResponseSuccess.wrapResponse(null, DealsStatus.OTP_MATCH, path);
//        return ResponseSuccess.wrap200(null, "OTP Match.",
//                "/api/auth/"+idUser+"/match-otp");
    }

    public ResponseEntity<JSONObject> forgotPassword(String idUser, ForgotPasswordRequest forgotPasswordRequest, String path){

        //Forgot password validation
        System.out.println("Forgot Password Validation. " +Parser.toJsonString(forgotPasswordRequest));
        ResponseEntity<JSONObject> check = validate.forgotPassword(forgotPasswordRequest, path);

        if (!check.getStatusCode().is2xxSuccessful()){
            return check;
        }
        if (sessionService.checkSession(idUser) != null){
            if (sessionService.checkSessionExpiredWithoutSession(idUser) > 0){
                sessionService.destroySession(idUser);
            }else {
                return ResponseFailed.wrapResponse(DealsStatus.ALREADY_LOGIN, path);
            }
        }

        JSONObject data = new JSONObject();
        data.put("password", forgotPasswordRequest.getPassword());
        data.put("newPassword", forgotPasswordRequest.getPassword());
        data.put("confirmPassword", forgotPasswordRequest.getConfirmPassword());
        //Forgot password validation in member domain
        System.out.println("Forgot Password. Send data to member domain : "+ Parser.toJsonString(forgotPasswordRequest));
        ResponseEntity<?> fromMember = member.forgotPassword(idUser, data);
        System.out.println("Forgot Password. Receive data from member domain :"+ fromMember.getBody().toString());

        JSONObject jsonMember = Parser.parseJSON(fromMember.getBody().toString());
        String message = ""+ jsonMember.get("message");
        String status = ""+jsonMember.get("status");

        //Check response
        if (!fromMember.getStatusCode().is2xxSuccessful()){
            if (fromMember.getBody().toString().toLowerCase().contains("connection refused")){
                return ResponseFailed.wrapResponse(DealsStatus.REQUEST_TIME_OUT, path);
            }
            return ResponseFailed.wrapResponseFailed(message, status, fromMember.getStatusCode(), path);
        }

        //Wrap response
        return ResponseSuccess.wrapResponse(null, DealsStatus.FORGOT_PASSWORD, path);
    }

}
