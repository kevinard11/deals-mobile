package com.okta.examples.service.validation;

import com.okta.examples.adapter.exception.SessionException;
import com.okta.examples.repository.MyBatisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class SessionValidation {

    @Autowired
    MyBatisRepository repository;

    public void request(String idUser, HttpServletRequest request){

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")){
            throw new SessionException("You are not authorized.", HttpStatus.UNAUTHORIZED);
        }

        String session = header.substring(7);
        if(!repository.checkSession(idUser).equals(session)){
            throw new SessionException("You are not authorized.", HttpStatus.UNAUTHORIZED);
        }

    }

}
