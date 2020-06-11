package com.okta.examples.service.usecase;

import com.okta.examples.adapter.status.MatchOtpException;
import com.okta.examples.adapter.wrapper.Parser;
import com.okta.examples.adapter.wrapper.ResponseSuccess;
import com.okta.examples.service.microservice.VoucherDomain;
import com.okta.examples.service.validation.VoucherValidation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class VoucherService {

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

        JSONArray voucher = (JSONArray) jsonVoucher.get("data");

        return ResponseSuccess.wrap200(voucher, "Success.",
                "/api/user/show-all-voucher");
    }

    public JSONObject filterVoucher(String merchantCategory, String page){

        ResponseEntity<?> fromVoucher = voucher.filterVoucher(merchantCategory, page);
        System.out.println("Filter Voucher. Receive data from voucher domain :"+ fromVoucher.getBody().toString());

        JSONObject jsonVoucher = Parser.parseJSON(fromVoucher.getBody().toString());
        String message = ""+ jsonVoucher.get("message");

        if (!fromVoucher.getStatusCode().is2xxSuccessful()){
            throw new MatchOtpException(message, fromVoucher.getStatusCode());
        }

        JSONArray voucher = (JSONArray) jsonVoucher.get("data");

        return ResponseSuccess.wrap200(voucher, "Success.",
                "/api/user/filter-voucher");
    }

    public JSONObject searchVoucher(String merchantName, String page){

        ResponseEntity<?> fromVoucher = voucher.searchVoucher(merchantName, page);
        System.out.println("Search Voucher. Receive data from voucher domain :"+ fromVoucher.getBody().toString());

        JSONObject jsonVoucher = Parser.parseJSON(fromVoucher.getBody().toString());
        String message = ""+ jsonVoucher.get("message");

        if (!fromVoucher.getStatusCode().is2xxSuccessful()){
            throw new MatchOtpException(message, fromVoucher.getStatusCode());
        }

        JSONArray voucher = (JSONArray) jsonVoucher.get("data");

        return ResponseSuccess.wrap200(voucher, "Success.",
                "/api/user/findByMerchantName-voucher");
    }

    public JSONObject sortVoucher(String name, String page){

        ResponseEntity<?> fromVoucher = voucher.sortVoucher(name, page);
        System.out.println("Sort Voucher. Receive data from voucher domain :"+ fromVoucher.getBody().toString());

        JSONObject jsonVoucher = Parser.parseJSON(fromVoucher.getBody().toString());
        String message = ""+ jsonVoucher.get("message");

        if (!fromVoucher.getStatusCode().is2xxSuccessful()){
            throw new MatchOtpException(message, fromVoucher.getStatusCode());
        }

        JSONArray voucher = (JSONArray) jsonVoucher.get("data");

        return ResponseSuccess.wrap200(voucher, "Success.",
                "/api/user/findByMerchantName-voucher");
    }
}
