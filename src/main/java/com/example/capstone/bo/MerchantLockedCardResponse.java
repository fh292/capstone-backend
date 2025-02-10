package com.example.capstone.bo;

import com.example.capstone.entities.CardEntity;

public class MerchantLockedCardResponse extends CardResponse {
    private String merchantName;

    public MerchantLockedCardResponse(CardEntity card) {
        super(card);
        this.merchantName = card.getMerchantName();
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
}
