//package com.okta.examples.adapter.jwt;
//
//import com.okta.examples.adapter.microservice.MemberDomain;
//import com.okta.examples.repository.MyBatisRepository;
//import com.okta.examples.service.exception.UnauthorizedException;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import javax.servlet.http.HttpSession;
//import java.util.ArrayList;
//import java.util.Date;
//
//@MapperScan("com.okta.examples.repository")
//@Service
//public class JwtUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    MyBatisRepository repository;
//
//    @Autowired
//    MemberDomain member;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        String[] split = username.split(";");
//        String tokenDetail = split[0];
//        String id = split[1];
//        System.out.println(id+" the token is : "+tokenDetail);
//        System.out.println("Save token. Send data to member domain : "+ id+" - " + tokenDetail);
//        if (!tokenDetail.equalsIgnoreCase(repository.checkToken(id))){
//            throw new UnauthorizedException("Unauthorized", HttpStatus.UNAUTHORIZED);
//        }
//        return new org.springframework.security.core.userdetails.User(tokenDetail+";"+id,
//                "deals",
//                new ArrayList<>());
//    }
//
//    public UserDetails loadBySession(HttpSession session){
////        System.out.println(session.getId());
//        System.out.println(new Date(session.getCreationTime()+(session.getMaxInactiveInterval()*1000)));
//
////        if (!session.getId().equalsIgnoreCase("84459582-0125-4c1d-aec1-a9ccbc21fed6")){
////            throw new UnauthorizedException("Unauthorized", HttpStatus.UNAUTHORIZED);
////        }
////        System.out.println("-----");
////        if ((new Date(session.getLastAccessedTime())).after(new Date(session.getCreationTime()+(session.getMaxInactiveInterval()*1000)))){
////            System.out.println("asdasdadasd");
////            session.invalidate();
////            throw new UnauthorizedException("Unauthorized", HttpStatus.UNAUTHORIZED);
////        }
//        return new org.springframework.security.core.userdetails.User("hai",
//                "$2a$10$0Ii1DG4cXE2lSKHiWTNtbOs0ZUAEXSzXPBtzGJQXtX0lzCjgHX3ue",
//                new ArrayList<>());
//    }
//
//    public UserDetails loadUserById(String tokenDetail, String id) {
//        return new org.springframework.security.core.userdetails.User(tokenDetail+";"+id,
//                "deals",
//                new ArrayList<>());
//    }
//}
