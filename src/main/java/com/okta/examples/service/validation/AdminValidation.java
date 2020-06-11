package com.okta.examples.service.validation;

import com.okta.examples.model.request.CreateMerchant;
import com.okta.examples.adapter.status.exception.CreateMerchantException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AdminValidation {

    public void createMerchant(CreateMerchant createMerchant){

        if (createMerchant.getVoucherName() == null || createMerchant.getVoucherPrice() == null ||
            createMerchant.getDiscount() == null || createMerchant.getMaxDiscount() == null ||
            createMerchant.getQuota()== null || createMerchant.getExpiredDate() == null ||
            createMerchant.getStatus() == null){
            throw new CreateMerchantException("Please fill in all the forms.", HttpStatus.BAD_REQUEST);
        }
    }
}
