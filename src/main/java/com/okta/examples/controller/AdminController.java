package com.okta.examples.controller;

import com.okta.examples.model.request.CreateMerchantRequest;
import com.okta.examples.model.response.ResponseFailed;
import com.okta.examples.service.usecase.AdminService;
import com.okta.examples.service.validation.SessionValidation;
import org.springframework.beans.factory.annotation.Autowired;
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
                                            @RequestBody CreateMerchantRequest createMerchantRequest,
                                            HttpServletRequest request){
        if (!sessionValidation.requestVoucher(request)){
            return ResponseFailed.unAuthorized(request.getServletPath());
        }
        return adminService.createMerchant(idUser, idMerchant, createMerchantRequest, request.getServletPath());
    }

    @GetMapping("/show-all-voucher")
    public ResponseEntity<?> getAllVoucher(@RequestParam(value = "page", required = false) String page,
                                           HttpServletRequest request) {
        if (!sessionValidation.requestVoucher(request)){
            return ResponseFailed.unAuthorized(request.getServletPath());
        }
        return adminService.getAllVoucher(page, request.getServletPath());
    }

    @GetMapping("/filterByStatus-voucher")
    public ResponseEntity<?> filterVoucher(@RequestParam("filterByStatus") String merchantCategory,
                                           @RequestParam("page") String page,
                                           HttpServletRequest request){
        if (!sessionValidation.requestVoucher(request)){
            return ResponseFailed.unAuthorized(request.getServletPath());
        }
        return adminService.filterVoucher(merchantCategory, page, request.getServletPath());
    }

    @GetMapping("/findByMerchantName-voucher")
    public ResponseEntity<?> searchVoucher(@RequestParam("merchantName") String merchantName,
                                           @RequestParam("page") String page,
                                           HttpServletRequest request){
        if (!sessionValidation.requestVoucher(request)){
            return ResponseFailed.unAuthorized(request.getServletPath());
        }
        return adminService.searchVoucher(merchantName, page, request.getServletPath());
    }

    @GetMapping("/sort-voucher")
    public ResponseEntity<?> sortVoucher(@RequestParam("sortBy") String name,
                                         @RequestParam("page") String page,
                                         HttpServletRequest request){
        if (!sessionValidation.requestVoucher(request)){
            return ResponseFailed.unAuthorized(request.getServletPath());
        }
        return adminService.sortVoucher(name, page, request.getServletPath());
    }

    @GetMapping("/voucher-detail-voucher/{idVoucher}")
    public ResponseEntity<?> voucherDetail(@PathVariable("idVoucher") String idVoucher,
                                           HttpServletRequest request){
        if (!sessionValidation.requestVoucher(request)){
            return ResponseFailed.unAuthorized(request.getServletPath());
        }
        return adminService.voucherDetail(idVoucher, request.getServletPath());
    }

}
