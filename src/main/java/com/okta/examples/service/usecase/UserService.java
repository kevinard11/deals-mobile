package com.okta.examples.service.usecase;

import com.okta.examples.adapter.dto.request.EditProfileRequest;
import com.okta.examples.adapter.exception.EditProfileException;
import com.okta.examples.adapter.exception.GetProfileException;
import com.okta.examples.adapter.wrapper.Parser;
import com.okta.examples.adapter.wrapper.ResponseSuccess;
import com.okta.examples.service.microservice.MemberDomain;
import com.okta.examples.service.validation.UserValidation;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    MemberDomain member;

    @Autowired
    UserValidation validate;

    @Autowired
    SessionService sessionService;

    public JSONObject getProfile(String idUser){

        //Get profil in member domain
        ResponseEntity<?> fromMember = member.getProfile(idUser);
        System.out.println("Profile. Receive data from member domain :"+ fromMember.getBody().toString());

        JSONObject jsonMember = Parser.parseJSON(fromMember.getBody().toString());
        String message = ""+ jsonMember.get("message");

        //Check response
        if (!fromMember.getStatusCode().is2xxSuccessful()){
            throw new GetProfileException(message, fromMember.getStatusCode());
        }

        JSONObject user = (JSONObject) jsonMember.get("data");

        //Wrap response
        return ResponseSuccess.wrap200(user, "Success",
                "/api/user/"+idUser);
    }

    public JSONObject editProfile(String idUser, EditProfileRequest editProfileRequest){

        //Edit profile validation
        validate.editProfile(editProfileRequest);

        //Edit profile validation in member domain
        System.out.println("Edit Profile. Send data to member domain : "+ Parser.toJsonString(editProfileRequest));
        ResponseEntity<?> fromMember = member.editProfile(idUser, editProfileRequest);
        System.out.println("Edit Profile. Receive data from member domain :"+ fromMember.getBody().toString());

        JSONObject jsonMember = Parser.parseJSON(fromMember.getBody().toString());
        String message = ""+ jsonMember.get("message");

        //Check response
        if (!fromMember.getStatusCode().is2xxSuccessful()){
            throw new EditProfileException(message, fromMember.getStatusCode());
        }

        JSONObject user = (JSONObject) jsonMember.get("data");

        //Wrap response
        return ResponseSuccess.wrap200(null, "Success",
                "/api/user/"+idUser);
    }

    public JSONObject logout(String idUser){

        //Logout in member domain
//        ResponseEntity<?> fromMember = member.logout(idUser);
        System.out.println("Logout.");
//
//        JSONObject jsonMember = Parser.parseJSON(fromMember.getBody().toString());
//        String message = ""+ jsonMember.get("message");
//
//        //Check response
//        if (!fromMember.getStatusCode().is2xxSuccessful()){
//            throw new LogoutException(message, fromMember.getStatusCode());
//        }

        //Destroy session
        System.out.println("Destroy Session for id :" + idUser);
        sessionService.destroySession(idUser);

        //Wrap response
        return ResponseSuccess.wrap200(null, "You are logged out",
                "/api/user/"+idUser);
    }

}
