package com.okta.examples.service.validation;

import com.okta.examples.adapter.status.CreateOrderException;
import com.okta.examples.adapter.status.PayOrderException;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class TransactionValidation {

    private final String regex_va = "^[\\d]{15,16}$";
    private final String regex_amount = "^(?=.*[\\d])(?!.*[\\D]).+$|^[\\d][.][\\d]+$";

    public void createOrder(JSONObject data){

        if (data.get("idVoucher") == null){
            throw new CreateOrderException("Please fill in all the forms.", HttpStatus.BAD_REQUEST);
        }
    }

    public void payOrder(JSONObject data){

        if (data.get("idTransaction") == null){
            throw new PayOrderException("Please fill in all the forms.", HttpStatus.BAD_REQUEST);
        }
    }

    public void payTopup(JSONObject data){

        if (data.get("virtualNumber") == null || data.get("amount") == null){
            throw new PayOrderException("Please fill in all the forms.", HttpStatus.BAD_REQUEST);
        }

        if (!Pattern.matches(regex_va, ""+data.get("virtualNumber"))){
            throw new PayOrderException("Virtual Number is invalid", HttpStatus.BAD_REQUEST);
        }

        if (!Pattern.matches(regex_amount,""+data.get("amount"))){
            throw new PayOrderException("Amount is invalid.", HttpStatus.BAD_REQUEST);
        }
    }
}
