package com.okta.examples.adapter.dto.response;

import com.okta.examples.model.Voucher;

public class VoucherListResponse {
    private Voucher voucher;

    public Voucher getVoucher() {
        return voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }
}
