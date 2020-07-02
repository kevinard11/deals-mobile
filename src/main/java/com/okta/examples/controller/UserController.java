package com.okta.examples.controller;

import com.okta.examples.model.request.EditProfileRequest;
import com.okta.examples.model.response.ResponseFailed;
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

    @GetMapping(value = "/{idUser}")
    public ResponseEntity<?> getProfile(@PathVariable("idUser") String idUser,
                                        HttpServletRequest request){
        if (!sessionValidation.request(idUser, request, "/api/user/"+idUser)){
            return ResponseFailed.unAuthorized(request.getServletPath());
        }
        return userService.getProfile(idUser, request.getServletPath());
    }

    @PutMapping(value = "/{idUser}")
    public ResponseEntity<?> editProfile(@PathVariable("idUser") String idUser,
                                         @RequestBody(required = false) EditProfileRequest editProfileRequest,
                                         HttpServletRequest request){
        if (!sessionValidation.request(idUser, request, "/api/user/"+idUser)){
            return ResponseFailed.unAuthorized(request.getServletPath());
        }
//        System.out.println(request.getSession().getAttribute("userId"));
        return userService.editProfile(idUser, editProfileRequest, request.getServletPath());
    }

    @PostMapping(value= "/{id_user}/transaction/voucher")
    public ResponseEntity<?> createOrderVoucher(@PathVariable("id_user") String idUser,
                                                @RequestBody(required = false) JSONObject data,
                                                HttpServletRequest request){
        if (!sessionValidation.request(idUser, request, "/api/user/"+idUser+"/transaction/voucher")){
            return ResponseFailed.unAuthorized(request.getServletPath());
        }
//        System.out.println(request.getSession().getAttribute("userId"));
        return transactionService.createOrderVoucher(idUser, data, request.getServletPath());
    }

    @PutMapping(value = "/{idUser}/transaction/voucher")
    public ResponseEntity<?> payOrderVoucher(@PathVariable("idUser") String idUser,
                                             @RequestBody(required = false) JSONObject data,
                                             HttpServletRequest request){
        if (!sessionValidation.request(idUser, request, "/api/user/"+idUser+"/transaction/voucher")){
            return ResponseFailed.unAuthorized(request.getServletPath());
        }
//        System.out.println(request.getSession().getAttribute("userId"));
        return transactionService.payOrderVoucher(idUser, data, request.getServletPath());
    }

    @PostMapping(value = "/{idUser}/transaction/topup")
    public ResponseEntity<?> payTopup(@PathVariable("idUser") String idUser,
                                      @RequestBody(required = false) JSONObject data,
                                      HttpServletRequest request){
        if (!sessionValidation.request(idUser, request, "/api/user/"+idUser+"/transaction/topup")){
            return ResponseFailed.unAuthorized(request.getServletPath());
        }
//        System.out.println(request.getSession().getAttribute("userId"));
        return transactionService.payTopup(idUser, data, request.getServletPath());
    }

    @GetMapping(value = "/{idUser}/transaction")
    public ResponseEntity<?> transactionHistory(@PathVariable("idUser") String idUser,
                                                @RequestParam(value = "category", required = false) String category,
                                                @RequestParam(value = "filter-start-date", required = false) String filterStart,
                                                @RequestParam(value = "filter-end-date", required = false) String filterEnd,
                                                @RequestParam(value = "page", required = false) String page,
                                                HttpServletRequest request){
        if (!sessionValidation.request(idUser, request, "/api/user/"+idUser+"/transaction")){
            return ResponseFailed.unAuthorized(request.getServletPath());
        }
//        System.out.println(request.getSession().getAttribute("userId"));
        return transactionService.transactionHistory(idUser, category, filterStart, filterEnd, page, request.getServletPath());
    }

    @GetMapping(value = "/{idUser}/transaction/{idTransaction}")
    public ResponseEntity<?> transactionDetail(@PathVariable("idUser") String idUser,
                                               @PathVariable("idTransaction") String idTransaction,
                                               HttpServletRequest request){
        if (!sessionValidation.request(idUser, request,"/api/user/"+idUser+"/transaction/"+idTransaction)){
            return ResponseFailed.unAuthorized(request.getServletPath());
        }
//        System.out.println(request.getSession().getAttribute("userId"));
        return transactionService.transactionDetail(idUser, idTransaction, request.getServletPath());
    }

    @GetMapping("/show-all-voucher")
    public ResponseEntity<?> getAllVoucher(@RequestParam(value = "page", required = false) String page,
                                           HttpServletRequest request) {
        if (!sessionValidation.requestId(request)){
            return ResponseFailed.unAuthorized(request.getServletPath());
        }
//        System.out.println(request.getSession().getAttribute("userId"));
        return voucherService.getAllVoucher(page, request.getServletPath());
    }

    @GetMapping("/filter-voucher")
    public ResponseEntity<?> filterVoucher(@RequestParam(value = "merchantCategory", required = false) String merchantCategory,
                                           @RequestParam(value = "page", required = false) String page,
                                           HttpServletRequest request){
        if (!sessionValidation.requestId(request)){
            return ResponseFailed.unAuthorized(request.getServletPath());
        }
//        System.out.println(request.getSession().getAttribute("userId"));
        return voucherService.filterVoucher(merchantCategory, page, request.getServletPath());
    }

    @GetMapping("/findByMerchantName-voucher")
    public ResponseEntity<?> searchVoucher(@RequestParam(value = "merchantName", required = false) String merchantName,
                                           @RequestParam(value = "page", required = false) String page,
                                           HttpServletRequest request){
        if (!sessionValidation.requestId(request)){
            return ResponseFailed.unAuthorized(request.getServletPath());
        }
//        System.out.println(request.getSession().getAttribute("userId"));
        return voucherService.searchVoucher(merchantName, page, request.getServletPath());
    }

    @GetMapping("/sort-voucher")
    public ResponseEntity<?> sortVoucher(@RequestParam(value = "sortBy", required = false) String name,
                                         @RequestParam(value = "page", required = false) String page,
                                         HttpServletRequest request){
        if (!sessionValidation.requestId(request)){
            return ResponseFailed.unAuthorized(request.getServletPath());
        }
//        System.out.println(request.getSession().getAttribute("userId"));
        return voucherService.sortVoucher(name, page, request.getServletPath());
    }

    @PostMapping("/{idUser}/logout")
    public ResponseEntity<?> logout(@PathVariable("idUser") String idUser,
                                    HttpServletRequest request){
        if (!sessionValidation.requestSession(idUser, request)){
            return ResponseFailed.unAuthorized(request.getServletPath());
        }
        return userService.logout(idUser, request.getServletPath());
    }

}
