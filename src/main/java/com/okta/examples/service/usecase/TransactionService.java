package com.okta.examples.service.usecase;

import com.okta.examples.adapter.exception.MatchOtpException;
import com.okta.examples.adapter.wrapper.Parser;
import com.okta.examples.adapter.wrapper.ResponseSuccess;
import com.okta.examples.service.microservice.OrderDomain;
import com.okta.examples.service.validation.TransactionValidation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    OrderDomain order;

    @Autowired
    TransactionValidation validate;

    public JSONObject createOrderVoucher(String idUser, JSONObject data){

        validate.createOrder(data);

        System.out.println("Create Order. Send data to order domain : "+ Parser.toJsonString(data));
        ResponseEntity<?> fromOrder = order.createOrder(idUser, data);
        System.out.println("Create Order. Receive data from order domain :"+ fromOrder.getBody().toString());

        JSONObject jsonOrder = Parser.parseJSON(fromOrder.getBody().toString());
        String message = ""+ jsonOrder.get("message");

        if (!fromOrder.getStatusCode().is2xxSuccessful()){
            throw new MatchOtpException(message, fromOrder.getStatusCode());
        }

        JSONObject order = (JSONObject) jsonOrder.get("data");

        return ResponseSuccess.wrap201(order, "Transaction has been created.",
                "/api/user/"+idUser+"/transaction/voucher");
    }

    public JSONObject payOrderVoucher(String idUser, JSONObject data){

        validate.payOrder(data);

        System.out.println("Pay Order. Send data to order domain : "+ Parser.toJsonString(data));
        ResponseEntity<?> fromOrder = order.payOrder(idUser, data);
        System.out.println("Pay Order. Receive data from order domain :"+ fromOrder.getBody().toString());

        JSONObject jsonOrder = Parser.parseJSON(fromOrder.getBody().toString());
        String message = ""+ jsonOrder.get("message");

        if (!fromOrder.getStatusCode().is2xxSuccessful()){
            throw new MatchOtpException(message, fromOrder.getStatusCode());
        }

        return ResponseSuccess.wrap200(null, "Your payment is successfull.",
                "/api/user/"+idUser+"/transaction/voucher");
    }

    public JSONObject payTopup(String idUser, JSONObject data){

        validate.payTopup(data);

        System.out.println("Pay TOP UP. Send data to order domain : "+ Parser.toJsonString(data));
        ResponseEntity<?> fromOrder = order.payTopup(idUser, data);
        System.out.println("Pay TOP UP. Receive data from order domain :"+ fromOrder.getBody().toString());

        JSONObject jsonOrder = Parser.parseJSON(fromOrder.getBody().toString());
        String message = ""+ jsonOrder.get("message");

        if (!fromOrder.getStatusCode().is2xxSuccessful()){
            throw new MatchOtpException(message, fromOrder.getStatusCode());
        }

        return ResponseSuccess.wrap200(null, "TOP UP completed successfully.",
                "/api/user/"+idUser+"/transaction/topup");
    }

    public JSONObject transactionHistory(String idUser, String category, String filterStart, String filterEnd, String page){

        ResponseEntity<?> fromOrder = order.transactionHistory(idUser, category, filterStart, filterEnd, page);
        System.out.println("Transaction History. Receive data from order domain :"+ fromOrder.getBody().toString());

        JSONObject jsonOrder = Parser.parseJSON(fromOrder.getBody().toString());
        String message = ""+ jsonOrder.get("message");

        if (!fromOrder.getStatusCode().is2xxSuccessful()){
            throw new MatchOtpException(message, fromOrder.getStatusCode());
        }

        JSONArray order = (JSONArray) jsonOrder.get("data");

        return ResponseSuccess.wrap200(order, "Transaction history are successfully collected.",
                "/api/user/"+idUser+"/transaction");
    }

    public JSONObject transactionDetail(String idUser, String idTransaction){

        ResponseEntity<?> fromOrder = order.transactionDetail(idUser, idTransaction);
        System.out.println("Transaction History. Receive data from order domain :"+ fromOrder.getBody().toString());

        JSONObject jsonOrder = Parser.parseJSON(fromOrder.getBody().toString());
        String message = ""+ jsonOrder.get("message");

        if (!fromOrder.getStatusCode().is2xxSuccessful()){
            throw new MatchOtpException(message, fromOrder.getStatusCode());
        }

        JSONArray order = (JSONArray) jsonOrder.get("data");

        return ResponseSuccess.wrap200(order, "Transaction history are successfully collected.",
                "/api/user/"+idUser+"/transaction/"+idTransaction);
    }
}
