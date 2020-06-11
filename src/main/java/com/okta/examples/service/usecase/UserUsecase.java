package com.okta.examples.service.usecase;

import com.okta.examples.model.request.RegisterRequest;
import com.okta.examples.model.User;
import com.okta.examples.repository.MyBatisRepository;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@MapperScan("com.okta.examples.repository")
@Service
public class UserUsecase {

    @Autowired
    MyBatisRepository repository;

    public int checkEmailandTelephone(String email, String telephone){ return repository.checkEmailandTelephone(email, telephone); }

    public void setActiveId(String id){ repository.setActiveId(id);}

    public void setActiveTelephone(String telephone){ repository.setActiveTelephone(telephone);}

    public void createUser(RegisterRequest registerRequest){
       repository.register(registerRequest);
    }

    public User findUserByTelephone(String telephone){ return repository.findByTelephone(telephone); }

    public String genereateId(){
        return UUID.randomUUID().toString();
    }

    public String encryptPassword(String password){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
//
    public boolean matchPassword(String password, String encryptPassword){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.matches(password, encryptPassword);
    }

    public void saveToken(String id, String token){ repository.saveToken(id, token);}

    public void updateToken(String id, String token){ repository.updateToken(id, token);}

    public void setTokeninValid(String id){ }
}
