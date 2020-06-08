package com.okta.examples.service.usecase;

import com.okta.examples.adapter.wrapper.Parser;
import com.okta.examples.adapter.wrapper.ResponseSuccess;
import com.okta.examples.service.microservice.MemberDomain;
import com.okta.examples.service.microservice.VoucherDomain;
import com.okta.examples.adapter.exception.MatchOtpException;
import com.okta.examples.service.validation.VoucherValidation;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class VoucherService {

    @Autowired
    MemberDomain member;

    @Autowired
    VoucherDomain voucher;

    @Autowired
    VoucherValidation validate;

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
                "/api/user/show-all-transaction");
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
                "/api/user/filter-voucher");
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
                "/api/user/findByMerchantName-voucher");
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
                "/api/user/findByMerchantName-voucher");
    }
}
