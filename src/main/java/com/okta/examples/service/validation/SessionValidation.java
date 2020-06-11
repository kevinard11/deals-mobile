package com.okta.examples.service.validation;

import com.okta.examples.adapter.status.DealsStatus;
import com.okta.examples.model.response.ResponseFailed;
import com.okta.examples.model.response.ResponseSuccess;
import com.okta.examples.repository.MyBatisRepository;
import com.okta.examples.service.usecase.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
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
            return ResponseFailed.wrapResponse(DealsStatus.NOT_AUTHORIZED, request.getServletPath());
        }

        String session = header.substring(7);
        String idSession = sessionService.checkSession(idUser);

        if (idSession == null){
            return ResponseFailed.wrapResponse(DealsStatus.NOT_AUTHORIZED, request.getServletPath());
        }

        if(!idSession.equals(session)){
            return ResponseFailed.wrapResponse(DealsStatus.NOT_AUTHORIZED, request.getServletPath());
        }

        if (sessionService.checkSessionExpired(idUser, idSession) == 0){
            sessionService.destroySession(idUser);
            return ResponseFailed.wrapResponse(DealsStatus.NOT_AUTHORIZED, request.getServletPath());
        }

        return ResponseSuccess.wrapOk();
    }

    public ResponseEntity<?> requestVoucher(HttpServletRequest request){

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")){
            return ResponseFailed.wrapResponse(DealsStatus.NOT_AUTHORIZED, request.getServletPath());
        }

        String idSession = header.substring(7);
        if (sessionService.checkSessionWithoutId(idSession) == 0){
            return ResponseFailed.wrapResponse(DealsStatus.NOT_AUTHORIZED, request.getServletPath());
        }

        if (sessionService.checkSessionExpiredWithoutId(idSession) == 0){
            sessionService.destroySessionWithoutId(idSession);
            return ResponseFailed.wrapResponse(DealsStatus.NOT_AUTHORIZED, request.getServletPath());
        }

        return ResponseSuccess.wrapOk();
    }

}
