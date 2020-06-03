package com.okta.examples.controller;

import com.okta.examples.adapter.dto.wrapper.ResponseFailed;
import com.okta.examples.adapter.dto.wrapper.ResponseSuccess;
import com.okta.examples.service.MemberDomain;
import com.okta.examples.service.OrderDomain;
import com.okta.examples.service.VoucherDomain;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    MemberDomain member;

    @Autowired
    VoucherDomain voucher;

    @Autowired
    OrderDomain order;

    @GetMapping(value = "/user/{idUser}")
    public ResponseEntity<?> getProfile(@PathVariable("idUser") String idUser) throws ParseException {

        ResponseEntity<?> fromMember = member.getProfile(idUser);

        JSONParser parser = new JSONParser();
        JSONObject jsonUser = (JSONObject) parser.parse(fromMember.getBody().toString());

        return new ResponseEntity<>(ResponseSuccess.wrap200(jsonUser, "success", "/user"+idUser),
                                HttpStatus.OK);
    }

    @PostMapping(value = "/user/{idUser}/transaction/voucher")
    public ResponseEntity<?> placeOrderVoucher(@PathVariable("idUser") String idUser
                    , @RequestBody JSONObject data) throws ParseException {

        System.out.println("Send data to order domain : "+ data.toJSONString());
        ResponseEntity<?> fromOrder = order.placeOrderVoucher(idUser, data);
        System.out.println("Receive data from order domain :"+ fromOrder.getBody().toString());

        JSONParser parser = new JSONParser();
        JSONObject jsonOrder = (JSONObject) parser.parse(fromOrder.getBody().toString());

        String message = ""+jsonOrder.get("message");

        if (message == null) {
            return new ResponseEntity<>(ResponseSuccess.wrap200(jsonOrder, message
                    , "/user" + idUser+"/transaction/voucher"),
                    HttpStatus.OK);
        }
        else {
           return new ResponseEntity<>(ResponseFailed.wrap400( null, message,
                   "/user" + idUser+"/transaction/voucher"), HttpStatus.BAD_REQUEST) ;
        }
    }

    @PostMapping(value = "/user/{idUser}/transaction/{idTransaction}/voucher")
    public ResponseEntity<?> paymentOrderVoucher(@PathVariable("idUser") String idUser
                                                 , @PathVariable("idTransaction") String idTransaction
                    , @RequestBody JSONObject data) throws ParseException {

        System.out.println("Send data to order domain : "+ data.toJSONString());
        ResponseEntity<?> fromOrder = order.paymentOrderVoucher(idUser, idTransaction, data);
        System.out.println("Receive data from order domain :"+ fromOrder.getBody().toString());

        JSONParser parser = new JSONParser();
        JSONObject jsonOrder = (JSONObject) parser.parse(fromOrder.getBody().toString());

        String message = ""+jsonOrder.get("message");

        if (message == null) {
            return new ResponseEntity<>(ResponseSuccess.wrap200(jsonOrder, message
                    , "/user" + idUser+"/transaction/voucher"),
                    HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(ResponseFailed.wrap400( null, message,
                    "/user" + idUser+"/transaction/voucher"), HttpStatus.BAD_REQUEST) ;
        }
    }

    @PostMapping(value = "/checkToken")
    public ResponseEntity<?> checkToken( @RequestBody JSONObject data) throws ParseException {

        System.out.println("Send data to order domain : "+ data.toJSONString());
        ResponseEntity<?> fromOrder = member.checkToken();
        System.out.println("Receive data from order domain :"+ fromOrder.getBody().toString());

        JSONParser parser = new JSONParser();
        JSONObject jsonOrder = new JSONObject();

        String message = ""+jsonOrder.get("message");
        System.out.println(message);
        if (jsonOrder.get("message") == null) {
            return new ResponseEntity<>(ResponseSuccess.wrap200(jsonOrder, fromOrder.getBody().toString()
                    , "/checkToken"),
                    HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(ResponseFailed.wrap400( null, message,
                    "/checkToken"), HttpStatus.BAD_REQUEST) ;
        }
    }
}
