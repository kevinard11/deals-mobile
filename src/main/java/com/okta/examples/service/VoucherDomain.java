package com.okta.examples.service;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class VoucherDomain {

    @Autowired
    private RestTemplate restTemplate;

    private final String api = "http://localhost:8080";

    public ResponseEntity<?> getVoucher(){
        return restTemplate.getForEntity(api+"/voucher", JSONObject.class);
    }
}
