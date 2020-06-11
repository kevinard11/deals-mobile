package com.okta.examples.service.usecase;

import com.okta.examples.repository.MyBatisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

    @Autowired
    MyBatisRepository repository;

    public String checkSession(String idUser){
        return repository.checkSession(idUser);
    }

    public void startSession(String idUser, String idSession){
        repository.startSession(idUser, idSession );
    }

    public void destroySession(String idUser){
        repository.destroySession(idUser);
    }

    public Integer checkSessionExpired(String idUser, String idSession){ return repository.checkSessionExpired(idUser, idSession);}

    public Integer checkSessionExpiredWithoutId(String idSession){ return repository.checkSessionExpiredWithoutId(idSession);}

    public Integer checkSessionWithoutId(String idSession){ return repository.checkSessionWithoutId(idSession);}

    public void destroySessionWithoutId(String idSession){ repository.destroySessionWithoutId(idSession);}


}
