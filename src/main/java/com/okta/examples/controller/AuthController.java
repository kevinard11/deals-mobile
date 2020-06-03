package com.okta.examples.controller;

import com.okta.examples.adapter.dto.request.LoginRequest;
import com.okta.examples.adapter.dto.request.RegisterRequest;
import com.okta.examples.adapter.dto.request.TokenRequest;
import com.okta.examples.adapter.dto.wrapper.ResponseFailed;
import com.okta.examples.adapter.dto.wrapper.ResponseSuccess;
import com.okta.examples.service.exception.LoginException;
import com.okta.examples.service.JwtService;
import com.okta.examples.service.MemberDomain;
import com.okta.examples.service.VoucherDomain;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
public class AuthController {

    @Autowired
    MemberDomain member;

    @Autowired
    VoucherDomain voucher;

    @Autowired
    JwtService jwtService;


    @GetMapping("/")
    public ResponseEntity<?> welcome() {
        return new ResponseEntity<>("Welcome", HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) throws ParseException {

        System.out.println("Send data to member domain : "+ registerRequest.toJsonString());
        ResponseEntity<?> fromMember = member.register(registerRequest);
        System.out.println("Receive data from member domain :"+ fromMember.getBody().toString());

        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(fromMember.getBody().toString());
        JSONObject result = new JSONObject();
        String message = ""+ obj.get("message");
        int status = Integer.parseInt(""+obj.get("status"));

        if (status == 400){
            throw new LoginException(message, HttpStatus.BAD_REQUEST);
        }
        if (status == 404){
            throw new LoginException(message, HttpStatus.NOT_FOUND);
        }
        else{
            String tokenDetail= UUID.randomUUID().toString();

            result.put("token", jwtService.generateToken(tokenDetail,""+obj.get("id")));

            TokenRequest tokenRequest = new TokenRequest();
            tokenRequest.setId(""+ obj.get("id"));
            tokenRequest.setToken(tokenDetail);

            member.saveToken(tokenRequest);

            ResponseEntity<?> fromVoucher = voucher.getVoucher();
            System.out.println("Receive data from voucher domain : "+ fromVoucher.getBody().toString());
            JSONObject obj3 =  (JSONObject) parser.parse(fromVoucher.getBody().toString());


            result.put("voucher", obj3);
            result.put("user", obj);
            return new ResponseEntity<>(
                    ResponseSuccess.wrap201(result, "Registration success", "/register"),
                    HttpStatus.CREATED);
        }
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) throws ParseException {

        System.out.println("Send data to member domain : "+ loginRequest.toJsonString());
        ResponseEntity<?> fromMember = member.login(loginRequest);
        System.out.println("Receive data from member domain :"+ fromMember.getBody().toString());

        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(fromMember.getBody().toString());
        JSONObject result = new JSONObject();

        String message = ""+obj.get("message");
        if (message.equalsIgnoreCase("Telephone Not Found")){
            return new ResponseEntity<>(
                    ResponseFailed.wrap404(null, message, "/login"), HttpStatus.NOT_FOUND);
        }
        else if (message.equalsIgnoreCase("Password wrong")){
            return new ResponseEntity<>(
                    ResponseFailed.wrap404(null, message, "/login"), HttpStatus.BAD_REQUEST);
        }
        else{
            ResponseEntity<?> fromVoucher = voucher.getVoucher();
            System.out.println("Receive data from voucher domain : "+ fromVoucher.getBody().toString());
            JSONObject objVoucher =  (JSONObject) parser.parse(fromVoucher.getBody().toString());
            JSONObject objUser = (JSONObject) obj.get("user");

            String tokenDetail= UUID.randomUUID().toString();
            TokenRequest tokenRequest = new TokenRequest();
            tokenRequest.setId(""+objUser.get("id"));
            tokenRequest.setToken(tokenDetail);
            member.saveToken(tokenRequest);

            String token = jwtService.generateToken(tokenDetail, ""+objUser.get("id"));

            result.put("token", token);
            result.put("voucher", objVoucher);
            result.put("user", objUser);

            return new ResponseEntity<>(
                    ResponseSuccess.wrap200(result, "You are Logged in", "/login"), HttpStatus.OK);
        }
    }

    @PostMapping(value = "/user/{idUser}/logout")
    public ResponseEntity<?> logout(@RequestBody JSONObject data
            , @PathVariable("idUser") String idUser ) throws ParseException {
        ResponseEntity<?> fromMember = member.logout(idUser);
        System.out.println("Receive data from member domain :"+ fromMember.getBody().toString());

        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(fromMember.getBody().toString());
        String message = ""+data.get("message");

        if (message.equalsIgnoreCase("User not logged in yet")){
            return new ResponseEntity<>(
                    ResponseFailed.wrap400(null, message,"/user/"+idUser+"/logout")
                    , HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity<>(
                    ResponseSuccess.wrap200(null, "User logged out succesfully","/user/"+idUser+"/logout")
                    , HttpStatus.CREATED);
        }
    }

    @PostMapping(value = "/request-otp")
    public ResponseEntity<?> requestOTP(@RequestBody JSONObject data) throws ParseException {

        System.out.println("Send data to member domain : "+ data.toJSONString());
        ResponseEntity<?> fromMember = member.requestOTP(data);
        System.out.println("Receive data from member domain : "+ fromMember.getBody().toString());

        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(fromMember.getBody().toString());
        String message = ""+obj.get("message");
        if (message.equalsIgnoreCase("Telephone not found")){
            return new ResponseEntity<>(
                    ResponseFailed.wrap404(null, message, "/request-otp"),
                    HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(
                    ResponseSuccess.wrap200(obj, "OTP send successfully", "/request-otp"),
                    HttpStatus.OK);
        }
    }

    @PostMapping(value = "/verification-otp")
    public ResponseEntity<?> verificationOTP(@RequestBody JSONObject data) throws ParseException {

        System.out.println("Send data to member domain : "+ data.toJSONString());
        ResponseEntity<?> fromMember = member.checkToken();
        System.out.println("Receive data from member domain : "+ fromMember.getBody().toString());

        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(fromMember.getBody().toString());
        String message = ""+obj.get("message");

        if (message.equalsIgnoreCase("OTP not found")){
            return new ResponseEntity<>(
                    ResponseFailed.wrap404(null, message, "/verification-otp"),
                    HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(
                    ResponseSuccess.wrap200(obj, "Your otp is correct", "/verification-otp"),
                    HttpStatus.OK);
        }
    }
}
