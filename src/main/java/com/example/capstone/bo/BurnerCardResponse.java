package com.example.capstone.bo;

import com.example.capstone.entities.CardEntity;

public class BurnerCardResponse extends CardResponse {
    private String durationLimit;



    public BurnerCardResponse(CardEntity card) {
        this.setId(card.getId());
        this.setCardName(card.getCardName());
        this.setCardType(card.getCardType());
        this.setBankAccountNumber(card.getBankAccountNumber());
        this.setSpendingLimit(card.getSpendingLimit());
        this.setRemainingLimit(card.getRemainingLimit());
        this.setExpiryDate(card.getExpiryDate());
        this.setCardColor(card.getCardColor());
        this.setCardIcon(card.getCardIcon());
        this.setPer_transaction(card.getPer_transaction());
        this.setPer_day(card.getPer_day());
        this.setPer_month(card.getPer_month());
        this.setPer_year(card.getPer_year());
        this.setTotal(card.getTotal());
        this.paused = card.getPaused();
        this.closed = card.getClosed();
        this.durationLimit = card.getDurationLimit();
    }

    public String getDurationLimit() {
        return durationLimit;
    }

    public void setDurationLimit(String durationLimit) {
        this.durationLimit = durationLimit;
    }
}
