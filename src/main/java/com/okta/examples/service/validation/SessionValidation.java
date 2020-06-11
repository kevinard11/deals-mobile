package com.okta.examples.service.validation;

import com.okta.examples.adapter.status.SessionException;
import com.okta.examples.repository.MyBatisRepository;
import com.okta.examples.service.usecase.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class SessionValidation {

    @Autowired
    MyBatisRepository repository;

    @Autowired
    SessionService sessionService;

    public ResponseEntity<?> request(String idUser, HttpServletRequest request){

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")){
            throw new SessionException("You are not authorized.", HttpStatus.UNAUTHORIZED);
        }

        String session = header.substring(7);
        String idSession = sessionService.checkSession(idUser);

        if (idSession == null){
            throw new SessionException("You are not authorized.", HttpStatus.UNAUTHORIZED);
        }

        if(!idSession.equals(session)){
            throw new SessionException("You are not authorized.", HttpStatus.UNAUTHORIZED);
        }

        if (sessionService.checkSessionExpired(idUser, idSession) == 0){
            sessionService.destroySession(idUser);
            throw new SessionException("You are not authorized.", HttpStatus.UNAUTHORIZED);
        }

        return null;
    }

    public ResponseEntity<?> requestVoucher(HttpServletRequest request){

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")){
            throw new SessionException("You are not authorized.", HttpStatus.UNAUTHORIZED);
        }

        String idSession = header.substring(7);
        if (sessionService.checkSessionWithoutId(idSession) == 0){
            throw new SessionException("You are not authorized.", HttpStatus.UNAUTHORIZED);
        }

        if (sessionService.checkSessionExpiredWithoutId(idSession) == 0){
            sessionService.destroySessionWithoutId(idSession);
            throw new SessionException("You are not authorized.", HttpStatus.UNAUTHORIZED);
        }

        return null;
    }

}
