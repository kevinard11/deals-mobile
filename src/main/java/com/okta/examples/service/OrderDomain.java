package com.okta.examples.service;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderDomain {

    @Autowired
    private RestTemplate restTemplate;

    private final String api = "http://localhost:8080";

    public ResponseEntity<?> placeOrderVoucher(String idUser, JSONObject data){
        ResponseEntity<?> result = null;
        try {
            result= restTemplate.postForEntity(api + "/user/"+idUser+"/transaction/voucher", data, JSONObject.class);
        }
        catch (HttpClientErrorException e){
            result = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
            System.out.println(e);
        }
        catch (HttpServerErrorException e){
            result = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
            System.out.println(e);
        }
        return result;
    }

    public ResponseEntity<?> paymentOrderVoucher(String idUser, String idTransaction, JSONObject data){
        ResponseEntity<?> result = null;
        try {
            result= restTemplate.postForEntity(api + "/user/"+idUser+"/transaction/"+idTransaction+"/voucher"
                                                , data, JSONObject.class);
        }
        catch (HttpClientErrorException e){
            result = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
            System.out.println(e);
        }
        catch (HttpServerErrorException e){
            result = new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.NOT_FOUND);
            System.out.println(e);
        }
        return result;
    }
}
