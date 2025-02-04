package com.example.capstone.bo;

public class MerchantLockedCardRequest extends CardRequest {
    private String merchantName;

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
}
