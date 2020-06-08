package com.okta.examples.service.validation;

import com.okta.examples.adapter.exception.CreateOrderException;
import com.okta.examples.adapter.exception.PayOrderException;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class TransactionValidation {

    public void createOrder(JSONObject data){
        if (data.get("idVoucher") == null){
            throw new CreateOrderException("Please fill in all the forms.", HttpStatus.BAD_REQUEST);
        }
    }

    public void payOrder(JSONObject data){
        if (data.get("idVoucher") == null){
            throw new PayOrderException("Please fill in all the forms.", HttpStatus.BAD_REQUEST);
        }
    }

    public void payTopup(JSONObject data){
        if (data.get("idMerchant") == null || data.get("amount") == null){
            throw new PayOrderException("Please fill in all the forms.", HttpStatus.BAD_REQUEST);
        }
    }
}
