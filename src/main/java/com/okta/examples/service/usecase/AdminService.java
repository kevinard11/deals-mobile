package com.okta.examples.service.usecase;

import com.okta.examples.adapter.dto.request.CreateMerchant;
import com.okta.examples.adapter.wrapper.Parser;
import com.okta.examples.adapter.wrapper.ResponseSuccess;
import com.okta.examples.service.microservice.VoucherDomain;
import com.okta.examples.adapter.exception.MatchOtpException;
import com.okta.examples.service.validation.AdminValidation;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AdminService {

    @Autowired
    VoucherDomain voucher;

    @Autowired
    AdminValidation validate;

    public JSONObject createMerchant(String idUser, String idMerchant, CreateMerchant createMerchant){

        System.out.println("Create Merchant. " +new Date());
        validate.createMerchant(createMerchant);

        System.out.println("Create Merchant. Send data to voucher domain :" + Parser.toJsonString(createMerchant));
        ResponseEntity<?> fromVoucher = voucher.createMerchant(idUser, idMerchant, createMerchant);
        System.out.println("Get All Voucher. Receive data from voucher domain :"+ fromVoucher.getBody().toString());

        JSONObject jsonVoucher = Parser.parseJSON(fromVoucher.getBody().toString());
        String message = ""+ jsonVoucher.get("message");

        if (!fromVoucher.getStatusCode().is2xxSuccessful()){
            throw new MatchOtpException(message, fromVoucher.getStatusCode());
        }

        JSONObject voucher = (JSONObject) jsonVoucher.get("data");

        return ResponseSuccess.wrap200(voucher, "Voucher successfully created",
                "/api/admin/"+idMerchant+"/merchant/"+idMerchant+"/vouchers");
    }

    public JSONObject getAllVoucher(String page){

        ResponseEntity<?> fromVoucher = voucher.getAllVoucher(page);
        System.out.println("Get All Voucher. Receive data from voucher domain :"+ fromVoucher.getBody().toString());

        JSONObject jsonVoucher = Parser.parseJSON(fromVoucher.getBody().toString());
        String message = ""+ jsonVoucher.get("message");

        if (!fromVoucher.getStatusCode().is2xxSuccessful()){
            throw new MatchOtpException(message, fromVoucher.getStatusCode());
        }

        JSONObject voucher = (JSONObject) jsonVoucher.get("data");

        return ResponseSuccess.wrap200(voucher, "Transaction history are successfully collected",
                "/api/admin/show-all-transaction");
    }

    public JSONObject filterVoucher(String merchantCategory, String page){

        ResponseEntity<?> fromVoucher = voucher.filterVoucher(merchantCategory, page);
        System.out.println("Filter Voucher. Receive data from voucher domain :"+ fromVoucher.getBody().toString());

        JSONObject jsonVoucher = Parser.parseJSON(fromVoucher.getBody().toString());
        String message = ""+ jsonVoucher.get("message");

        if (!fromVoucher.getStatusCode().is2xxSuccessful()){
            throw new MatchOtpException(message, fromVoucher.getStatusCode());
        }

        JSONObject voucher = (JSONObject) jsonVoucher.get("data");

        return ResponseSuccess.wrap200(voucher, "Success",
                "/api/admin/filter-voucher");
    }

    public JSONObject searchVoucher(String merchantName, String page){

        ResponseEntity<?> fromVoucher = voucher.searchVoucher(merchantName, page);
        System.out.println("Filter Voucher. Receive data from voucher domain :"+ fromVoucher.getBody().toString());

        JSONObject jsonVoucher = Parser.parseJSON(fromVoucher.getBody().toString());
        String message = ""+ jsonVoucher.get("message");

        if (!fromVoucher.getStatusCode().is2xxSuccessful()){
            throw new MatchOtpException(message, fromVoucher.getStatusCode());
        }

        JSONObject voucher = (JSONObject) jsonVoucher.get("data");

        return ResponseSuccess.wrap200(voucher, "Success",
                "/api/admin/findByMerchantName-voucher");
    }

    public JSONObject sortVoucher(String name, String page){

        ResponseEntity<?> fromVoucher = voucher.sortVoucher(name, page);
        System.out.println("Filter Voucher. Receive data from voucher domain :"+ fromVoucher.getBody().toString());

        JSONObject jsonVoucher = Parser.parseJSON(fromVoucher.getBody().toString());
        String message = ""+ jsonVoucher.get("message");

        if (!fromVoucher.getStatusCode().is2xxSuccessful()){
            throw new MatchOtpException(message, fromVoucher.getStatusCode());
        }

        JSONObject voucher = (JSONObject) jsonVoucher.get("data");

        return ResponseSuccess.wrap200(voucher, "Success",
                "/api/admin/findByMerchantName-voucher");
    }
}
