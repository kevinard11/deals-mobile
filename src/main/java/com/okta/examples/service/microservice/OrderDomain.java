package com.okta.examples.service.microservice;

import com.okta.examples.adapter.template.Template;
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

    @Autowired
    private Template template;

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

    public ResponseEntity<?> createOrder(String idUser, JSONObject data){
        return template.post(api+"/api/user/"+idUser+"/transaction/voucher", data);
    }

    public ResponseEntity<?> payOrder(String idUser, JSONObject data){
        return template.put(api+"/api/user/"+idUser+"/transaction/voucher", data);
    }

    public ResponseEntity<?> payTopup(String idUser, JSONObject data){
        return template.post(api+"/api/user/"+idUser+"/transaction/topup", data);
    }

    public ResponseEntity<?> transactionHistory(String idUser, String category, String filterStart, String filterEnd, String page){
        return template.get(api+"/api/user/"+idUser+"/transaction" +
                            "?category=" +category+"&" +
                            "filter-start-date=" +filterStart +"&" +
                            "filter-end-date=" +filterEnd+"&" +
                            "page=" +page);
    }

    public ResponseEntity<?> transactionDetail(String idUser, String idTransaction){
        return template.get(api+"/api/user/"+idUser+"/transaction/"+idTransaction);
    }
}
