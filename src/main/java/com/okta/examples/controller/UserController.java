package com.okta.examples.controller;

import com.okta.examples.adapter.dto.request.EditProfileRequest;
import com.okta.examples.service.usecase.TransactionService;
import com.okta.examples.service.usecase.UserService;
import com.okta.examples.service.usecase.VoucherService;
import com.okta.examples.service.validation.SessionValidation;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    VoucherService voucherService;

    @Autowired
    SessionValidation sessionValidation;

    @PostMapping(value = "/{idUser}")
    public ResponseEntity<?> welcome(@PathVariable("idUser") String idUser,
                                     HttpServletRequest request){
        sessionValidation.request(idUser, request);
        return new ResponseEntity<>("Welcome. Your session id : "+idUser, HttpStatus.OK);
    }

    @GetMapping(value = "/{id_user}")
    public ResponseEntity<?> getProfile(@PathVariable("id_user") String idUser,
                                        HttpServletRequest request){
        sessionValidation.request(idUser, request);
        return new ResponseEntity<>(userService.getProfile(idUser), HttpStatus.OK);
    }

    @PutMapping(value = "/{idUser}")
    public ResponseEntity<?> editProfile(@PathVariable("idUser") String idUser,
                                         @RequestBody EditProfileRequest editProfileRequest,
                                         HttpServletRequest request){
        sessionValidation.request(idUser, request);
        return new ResponseEntity<>(userService.editProfile(idUser, editProfileRequest), HttpStatus.OK);
    }

    @PostMapping(value= "/{id_user}/transaction/voucher")
    public ResponseEntity<?> createOrderVoucher(@PathVariable("id_user") String idUser,
                                                @RequestBody JSONObject data,
                                                HttpServletRequest request){
//        sessionValidation.request(idUser, request);
        return new ResponseEntity<>(transactionService.createOrderVoucher(idUser, data), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{idUser}/transaction/voucher")
    public ResponseEntity<?> payOrderVoucher(@PathVariable("idUser") String idUser,
                                             @RequestBody JSONObject data,
                                             HttpServletRequest request){
//        sessionValidation.request(idUser, request);
        return new ResponseEntity<>(transactionService.payOrderVoucher(idUser, data), HttpStatus.OK);
    }

    @PostMapping(value = "/{idUser}/transaction/topup")
    public ResponseEntity<?> payTopup(@PathVariable("idUser") String idUser,
                                      @RequestBody JSONObject data,
                                      HttpServletRequest request){
//        sessionValidation.request(idUser, request);
        return new ResponseEntity<>(transactionService.payTopup(idUser, data), HttpStatus.OK);
    }

    @GetMapping(value = "/{idUser}/transaction")
    public ResponseEntity<?> transactionHistory(@PathVariable("idUser") String idUser,
                                                @RequestParam(value = "category", required = false) String category,
                                                @RequestParam(value = "filter-start-date", required = false) String filterStart,
                                                @RequestParam(value = "filter-end-date", required = false) String filterEnd,
                                                @RequestParam(value = "page", required = false) Integer page,
                                                HttpServletRequest request){
//        sessionValidation.request(idUser, request);
        return new ResponseEntity<>(transactionService.transactionHistory(idUser, category, filterStart, filterEnd, page),
                                    HttpStatus.OK);
    }

    @GetMapping(value = "/{idUser}/transaction/{idTransaction}")
    public ResponseEntity<?> transactionDetail(@PathVariable("idUser") String idUser,
                                               @PathVariable("idTransaction") String idTransaction,
                                               HttpServletRequest request){
//        sessionValidation.request(idUser, request);
        return new ResponseEntity<>(transactionService.transactionDetail(idUser, idTransaction),
                HttpStatus.OK);
    }

    @GetMapping("/{idUser}/show-all-voucher")
    public ResponseEntity<?> getAllVoucher(@PathVariable("idUser") String idUser,
                                           @RequestParam("page") String page,
                                           HttpServletRequest request) {
//        sessionValidation.request(idUser, request);
        return new ResponseEntity<>(voucherService.getAllVoucher(page), HttpStatus.OK);
    }

    @GetMapping("/{idUser}/filter-voucher")
    public ResponseEntity<?> filterVoucher(@PathVariable("idUser") String idUser,
                                           @RequestParam("merchantCategory") String merchantCategory,
                                           @RequestParam("page") String page,
                                           HttpServletRequest request){
//        sessionValidation.request(idUser, request);
        return new ResponseEntity<>(voucherService.filterVoucher(merchantCategory, page), HttpStatus.OK);
    }

    @GetMapping("/{idUser}/findByMerchantName-voucher")
    public ResponseEntity<?> searchVoucher(@PathVariable("idUser") String idUser,
                                           @RequestParam("merchantName") String merchantName,
                                           @RequestParam("page") String page,
                                           HttpServletRequest request){
//        sessionValidation.request(idUser, request);
        return new ResponseEntity<>(voucherService.searchVoucher(merchantName, page), HttpStatus.OK);
    }

    @GetMapping("/{idUser}/sort-voucher")
    public ResponseEntity<?> sortVoucher(@PathVariable("idUser") String idUser,
                                         @RequestParam("sortBy") String name,
                                         @RequestParam("page") String page,
                                         HttpServletRequest request){
//        sessionValidation.request(idUser, request);
        return new ResponseEntity<>(voucherService.sortVoucher(name, page), HttpStatus.OK);
    }

    @PostMapping("/{idUser}/logout")
    public ResponseEntity<?> logout(@PathVariable("idUser") String idUser,
                                    HttpServletRequest request){
//        sessionValidation.request(idUser, request);
        return new ResponseEntity<>(userService.logout(idUser), HttpStatus.OK);
    }

}
