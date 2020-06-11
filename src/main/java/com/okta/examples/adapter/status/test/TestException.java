package com.okta.examples.adapter.status.test;

import com.okta.examples.adapter.status.DealsStatus;

public class TestException extends ResponseDealsStatusException {

    public TestException(DealsStatus exceptionCode) {
        super(exceptionCode);
        System.out.println(String.format("%03d",exceptionCode.getValue())+". "+exceptionCode.getMessage());
    }
}
