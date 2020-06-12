package com.okta.examples.service.usecase;

import com.okta.examples.adapter.status.DealsStatus;
import com.okta.examples.adapter.parser.Parser;
import com.okta.examples.model.response.ResponseFailed;
import com.okta.examples.model.response.ResponseSuccess;
import com.okta.examples.service.microservice.OrderDomain;
import com.okta.examples.service.validation.TransactionValidation;
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

    public ResponseEntity<?> createOrderVoucher(String idUser, JSONObject data, String path){

        ResponseEntity<?> check = validate.createOrder(data, path);
        if (!check.getStatusCode().is2xxSuccessful()){
            return check;
        }

        System.out.println("Create Order. Send data to order domain : "+ Parser.toJsonString(data));
        ResponseEntity<?> fromOrder = order.createOrder(idUser, data);
        System.out.println("Create Order. Receive data from order domain :"+ fromOrder.getBody().toString());

        JSONObject jsonOrder = Parser.parseJSON(fromOrder.getBody().toString());
        String message = ""+ jsonOrder.get("message");
        String status = ""+ jsonOrder.get("status");

        if (!fromOrder.getStatusCode().is2xxSuccessful()){
            return ResponseFailed.wrapResponseFailed(message, status, fromOrder.getStatusCode(), path);
        }

        JSONObject order = (JSONObject) jsonOrder.get("data");

        return ResponseSuccess.wrapResponse(order, DealsStatus.TRANSACTION_CREATED, path);
    }

    public ResponseEntity<?> payOrderVoucher(String idUser, JSONObject data, String path){

        ResponseEntity<?> check = validate.payOrder(data, path);
        if (!check.getStatusCode().is2xxSuccessful()){
            return check;
        }

        System.out.println("Pay Order. Send data to order domain : "+ Parser.toJsonString(data));
        ResponseEntity<?> fromOrder = order.payOrder(idUser, data);
        System.out.println("Pay Order. Receive data from order domain :"+ fromOrder.getBody().toString());

        JSONObject jsonOrder = Parser.parseJSON(fromOrder.getBody().toString());
        String message = ""+ jsonOrder.get("message");
        String status = ""+ jsonOrder.get("status");

        if (!fromOrder.getStatusCode().is2xxSuccessful()){
            return ResponseFailed.wrapResponseFailed(message, status, fromOrder.getStatusCode(), path);
        }

        return ResponseSuccess.wrapResponse(null, DealsStatus.PAYMENT_SUCCESS, path);
//        return ResponseSuccess.wrap200(null, "Your payment is successfull.",
//                "/api/user/"+idUser+"/transaction/voucher");
    }

    public ResponseEntity<?> payTopup(String idUser, JSONObject data, String path){

        ResponseEntity<?> check = validate.payTopup(data, path);
        if (!check.getStatusCode().is2xxSuccessful()){
            return check;
        }

        System.out.println("Pay TOP UP. Send data to order domain : "+ Parser.toJsonString(data));
        ResponseEntity<?> fromOrder = order.payTopup(idUser, data);
        System.out.println("Pay TOP UP. Receive data from order domain :"+ fromOrder.getBody().toString());

        JSONObject jsonOrder = Parser.parseJSON(fromOrder.getBody().toString());
        String message = ""+ jsonOrder.get("message");
        String status = ""+ jsonOrder.get("status");

        if (!fromOrder.getStatusCode().is2xxSuccessful()){
            return ResponseFailed.wrapResponseFailed(message, status, fromOrder.getStatusCode(), path);
        }

        return ResponseSuccess.wrapResponse(null, DealsStatus.PAYMENT_SUCCESS, path);

//        return ResponseSuccess.wrap200(null, "TOP UP completed successfully.",
//                "/api/user/"+idUser+"/transaction/topup");
    }

    public ResponseEntity<?> transactionHistory(String idUser, String category, String filterStart, String filterEnd, Integer page, String path){

        ResponseEntity<?> fromOrder = order.transactionHistory(idUser, category, filterStart, filterEnd, page, path);
        System.out.println("Transaction History. Receive data from order domain :"+ fromOrder.getBody().toString());

        JSONObject jsonOrder = Parser.parseJSON(fromOrder.getBody().toString());
        String message = ""+ jsonOrder.get("message");
        String status = ""+ jsonOrder.get("status");

        if (!fromOrder.getStatusCode().is2xxSuccessful()){
            return ResponseFailed.wrapResponseFailed(message, status, fromOrder.getStatusCode(), path);
        }

        JSONObject order = (JSONObject) jsonOrder.get("data");

        return ResponseSuccess.wrapResponse(order, DealsStatus.TRANSACTION_HISTORY_COLLECTED, path);
//        return ResponseSuccess.wrap200(order, "Transaction history are successfully collected.",
//                "/api/user/"+idUser+"/transaction");
    }

    public ResponseEntity<?> transactionDetail(String idUser, String idTransaction, String path){

        ResponseEntity<?> fromOrder = order.transactionDetail(idUser, idTransaction);
        System.out.println("Transaction History. Receive data from order domain :"+ fromOrder.getBody().toString());

        JSONObject jsonOrder = Parser.parseJSON(fromOrder.getBody().toString());
        String message = ""+ jsonOrder.get("message");
        String status = ""+ jsonOrder.get("status");

        if (!fromOrder.getStatusCode().is2xxSuccessful()){
            return ResponseFailed.wrapResponseFailed(message, status, fromOrder.getStatusCode(), path);
        }

        JSONObject order = (JSONObject) jsonOrder.get("data");

        return ResponseSuccess.wrapResponse(order, DealsStatus.TRANSACTION_HISTORY_COLLECTED, path);
//        return ResponseSuccess.wrap200(order, "Transaction history are successfully collected.",
//                "/api/user/"+idUser+"/transaction/"+idTransaction);
    }
}
