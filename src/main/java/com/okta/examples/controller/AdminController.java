package com.okta.examples.controller;

import com.okta.examples.adapter.dto.request.CreateMerchant;
import com.okta.examples.service.usecase.AdminService;
import com.okta.examples.service.validation.SessionValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    SessionValidation sessionValidation;

    @PostMapping(value = "/{idUser}/merchant/{idMerchant}/vouchers")
    public ResponseEntity<?> createMerchant(@PathVariable("idUser") String idUser,
                                            @PathVariable("idMerchant") String idMerchant,
                                            @RequestBody CreateMerchant createMerchant,
                                            HttpServletRequest request){
//        sessionValidation.request(idUser, request);
        return new ResponseEntity<>(adminService.createMerchant(idUser, idMerchant, createMerchant), HttpStatus.CREATED);
    }

    @GetMapping("/{idUser}/show-all-voucher")
    public ResponseEntity<?> getAllVoucher(@PathVariable("idUser") String idUser,
                                           @RequestParam("page") String page,
                                           HttpServletRequest request) {
//        sessionValidation.request(idUser, request);
        return new ResponseEntity<>(adminService.getAllVoucher(page), HttpStatus.OK);
    }

    @GetMapping("/{idUser}/filter-voucher")
    public ResponseEntity<?> filterVoucher(@PathVariable("idUser") String idUser,
                                           @RequestParam("merchantCategory") String merchantCategory,
                                           @RequestParam("page") String page,
                                           HttpServletRequest request){
//        sessionValidation.request(idUser, request);
        return new ResponseEntity<>(adminService.filterVoucher(merchantCategory, page), HttpStatus.OK);
    }

    @GetMapping("/{idUser}/findByMerchantName-voucher")
    public ResponseEntity<?> searchVoucher(@PathVariable("idUser") String idUser,
                                           @RequestParam("merchantName") String merchantName,
                                           @RequestParam("page") String page,
                                           HttpServletRequest request){
//        sessionValidation.request(idUser, request);
        return new ResponseEntity<>(adminService.searchVoucher(merchantName, page), HttpStatus.OK);
    }

    @GetMapping("/{idUser}/sort-voucher")
    public ResponseEntity<?> sortVoucher(@PathVariable("idUser") String idUser,
                                         @RequestParam("sortBy") String name,
                                         @RequestParam("page") String page,
                                         HttpServletRequest request){
//        sessionValidation.request(idUser, request);
        return new ResponseEntity<>(adminService.sortVoucher(name, page), HttpStatus.OK);
    }

    @GetMapping("/{idUser}/voucher-detail-voucher/{idVoucher}")
    public ResponseEntity<?> voucherDetail(@PathVariable("idUser") String idUser,
                                           @PathVariable("idVoucher") String idVoucher,
                                           HttpServletRequest request){
//        sessionValidation.request(idUser, request);
        return new ResponseEntity<>(adminService.voucherDetail(idVoucher), HttpStatus.OK);
    }
}
