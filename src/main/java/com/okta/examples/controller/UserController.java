package com.okta.examples.controller;

import com.okta.examples.model.request.EditProfileRequest;
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
        ResponseEntity<?> check = sessionValidation.request(idUser, request);
        if (!check.getStatusCode().is2xxSuccessful()){
            return check;
        }
        return new ResponseEntity<>("Welcome. Your session id : "+idUser, HttpStatus.OK);
    }

    @GetMapping(value = "/{id_user}")
    public ResponseEntity<?> getProfile(@PathVariable("id_user") String idUser,
                                        HttpServletRequest request){
//        ResponseEntity<?> checkSessiom = sessionValidation.request(idUser, request);
//        if (!checkSessiom.getStatusCode().is2xxSuccessful()){
//            return checkSessiom;
//        }
        return userService.getProfile(idUser, request.getServletPath());
    }

    @PutMapping(value = "/{idUser}")
    public ResponseEntity<?> editProfile(@PathVariable("idUser") String idUser,
                                         @RequestBody EditProfileRequest editProfileRequest,
                                         HttpServletRequest request){
//        ResponseEntity<?> checkSessiom = sessionValidation.request(idUser, request);
//        if (!checkSessiom.getStatusCode().is2xxSuccessful()){
//            return checkSessiom;
//        }
        return userService.editProfile(idUser, editProfileRequest, request.getServletPath());
    }

    @PostMapping(value= "/{id_user}/transaction/voucher")
    public ResponseEntity<?> createOrderVoucher(@PathVariable("id_user") String idUser,
                                                @RequestBody JSONObject data,
                                                HttpServletRequest request){
//        ResponseEntity<?> checkSessiom = sessionValidation.request(idUser, request);
//        if (!checkSessiom.getStatusCode().is2xxSuccessful()){
//            return checkSessiom;
//        }
        return transactionService.createOrderVoucher(idUser, data, request.getServletPath());
    }

    @PutMapping(value = "/{idUser}/transaction/voucher")
    public ResponseEntity<?> payOrderVoucher(@PathVariable("idUser") String idUser,
                                             @RequestBody JSONObject data,
                                             HttpServletRequest request){
//        sessionValidation.request(idUser, request);
        return transactionService.payOrderVoucher(idUser, data, request.getServletPath());
    }

    @PostMapping(value = "/{idUser}/transaction/topup")
    public ResponseEntity<?> payTopup(@PathVariable("idUser") String idUser,
                                      @RequestBody JSONObject data,
                                      HttpServletRequest request){
//        sessionValidation.request(idUser, request);
        return transactionService.payTopup(idUser, data, request.getServletPath());
    }

    @GetMapping(value = "/{idUser}/transaction")
    public ResponseEntity<?> transactionHistory(@PathVariable("idUser") String idUser,
                                                @RequestParam(value = "category", required = false) String category,
                                                @RequestParam(value = "filter-start-date", required = false) String filterStart,
                                                @RequestParam(value = "filter-end-date", required = false) String filterEnd,
                                                @RequestParam(value = "page", required = false) Integer page,
                                                HttpServletRequest request){
        ResponseEntity<?> checkSessiom = sessionValidation.requestVoucher(request);
        if (!checkSessiom.getStatusCode().is2xxSuccessful()){
            return checkSessiom;
        }
        System.out.println(request.getServletPath());
        return transactionService.transactionHistory(idUser, category, filterStart, filterEnd, page, request.getServletPath());
    }

    @GetMapping(value = "/{idUser}/transaction/{idTransaction}")
    public ResponseEntity<?> transactionDetail(@PathVariable("idUser") String idUser,
                                               @PathVariable("idTransaction") String idTransaction,
                                               HttpServletRequest request){
//        sessionValidation.request(idUser, request);
        return transactionService.transactionDetail(idUser, idTransaction, request.getServletPath());
    }

    @GetMapping("/show-all-voucher")
    public ResponseEntity<?> getAllVoucher(@RequestParam(value = "page", required = false) String page,
                                           HttpServletRequest request) {
//        ResponseEntity<?> check = sessionValidation.requestVoucher(request);
//        if (!check.getStatusCode().is2xxSuccessful()){
//            return check;
//        }
        return voucherService.getAllVoucher(page, request.getServletPath());
    }

    @GetMapping("/filter-voucher")
    public ResponseEntity<?> filterVoucher(@RequestParam(value = "merchantCategory", required = false) String merchantCategory,
                                           @RequestParam(value = "page", required = false) String page,
                                           HttpServletRequest request){
////        sessionValidation.requestVoucher(request);
        return new ResponseEntity<>(voucherService.filterVoucher(merchantCategory, page, request.getServletPath()), HttpStatus.OK);
    }

    @GetMapping("/findByMerchantName-voucher")
    public ResponseEntity<?> searchVoucher(@RequestParam(value = "merchantName", required = false) String merchantName,
                                           @RequestParam(value = "page", required = false) String page,
                                           HttpServletRequest request){
        sessionValidation.requestVoucher(request);
        return new ResponseEntity<>(voucherService.searchVoucher(merchantName, page), HttpStatus.OK);
    }

    @GetMapping("/sort-voucher")
    public ResponseEntity<?> sortVoucher(@RequestParam(value = "sortBy", required = false) String name,
                                         @RequestParam(value = "page", required = false) String page,
                                         HttpServletRequest request){
        sessionValidation.requestVoucher(request);
        return new ResponseEntity<>(voucherService.sortVoucher(name, page), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@PathVariable("idUser") String idUser,
                                    HttpServletRequest request){
//        sessionValidation.request(idUser, request);
        return userService.logout(idUser, request.getServletPath());
    }

}
