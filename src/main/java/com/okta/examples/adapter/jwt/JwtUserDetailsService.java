package com.okta.examples.adapter.jwt;

import com.okta.examples.service.exception.UnauthorizedException;
import com.okta.examples.repository.MyBatisRepository;
import com.okta.examples.service.MemberDomain;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@MapperScan("com.okta.examples.repository")
@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    MyBatisRepository repository;

    @Autowired
    MemberDomain member;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String[] split = username.split(";");
        String tokenDetail = split[0];
        String id = split[1];
        //System.out.println(id+" the token is : "+tokenDetail);
        if (!tokenDetail.equalsIgnoreCase(repository.checkToken(id))){
            throw new UnauthorizedException("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
        return new org.springframework.security.core.userdetails.User(tokenDetail+";"+id,
                "$2a$10$0Ii1DG4cXE2lSKHiWTNtbOs0ZUAEXSzXPBtzGJQXtX0lzCjgHX3ue",
                new ArrayList<>());
    }

    public UserDetails loadUserById(String tokenDetail, String id) {
        return new org.springframework.security.core.userdetails.User(tokenDetail+";"+id,
                "$2a$10$0Ii1DG4cXE2lSKHiWTNtbOs0ZUAEXSzXPBtzGJQXtX0lzCjgHX3ue",
                new ArrayList<>());
    }
}
