package com.example.capstone.bo;

import com.example.capstone.entities.CardEntity;

public class CategoryLockedCardResponse extends CardResponse {
    private String categoryName;

    public CategoryLockedCardResponse(CardEntity card) {
        super(card);
        // this.setId(card.getId());
        // this.setCardName(card.getCardName());
        // this.setCardType(card.getCardType());
        // this.setBankAccountNumber(card.getBankAccountNumber());
        // this.setSpendingLimit(card.getSpendingLimit());
        // this.setRemainingLimit(card.getRemainingLimit());
        // this.setExpiryDate(card.getExpiryDate());
        // this.setCardColor(card.getCardColor());
        // this.setCardIcon(card.getCardIcon());
        // this.setPer_transaction(card.getPer_transaction());
        // this.setPer_day(card.getPer_day());
        // this.setPer_week(card.getPer_week());
        // this.setPer_month(card.getPer_month());
        // this.setPer_year(card.getPer_year());
        // this.setTotal(card.getTotal());
        // this.paused = card.getPaused();
        // this.closed = card.getClosed();
        this.categoryName = card.getCategoryName();
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
