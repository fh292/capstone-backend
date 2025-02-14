package com.example.capstone.bo;

import java.time.LocalDateTime;

import com.example.capstone.entities.SubscriptionEntity;

public class SubscriptionResponse {
    private Long id;
    private String merchant;
    private Double amount;
    private String category;
    private LocalDateTime lastChargeDate;
    private LocalDateTime nextChargeDate;
    private String billingCycle;
    private Boolean isActive;
    private Long cardId;
    private String cardName;
    private Boolean isCardPaused;

    public SubscriptionResponse(SubscriptionEntity entity) {
        this.id = entity.getId();
        this.merchant = entity.getMerchant();
        this.amount = entity.getAmount();
        this.category = entity.getCategory();
        this.lastChargeDate = entity.getLastChargeDate();
        this.nextChargeDate = entity.getNextChargeDate();
        this.billingCycle = entity.getBillingCycle();
        this.isActive = entity.getIsActive();
        this.cardId = entity.getCard().getId();
        this.cardName = entity.getCard().getCardName();
        this.isCardPaused = entity.getCard().getPaused();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getLastChargeDate() {
        return lastChargeDate;
    }

    public void setLastChargeDate(LocalDateTime lastChargeDate) {
        this.lastChargeDate = lastChargeDate;
    }

    public LocalDateTime getNextChargeDate() {
        return nextChargeDate;
    }

    public void setNextChargeDate(LocalDateTime nextChargeDate) {
        this.nextChargeDate = nextChargeDate;
    }

    public String getBillingCycle() {
        return billingCycle;
    }

    public void setBillingCycle(String billingCycle) {
        this.billingCycle = billingCycle;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public Boolean getIsCardPaused() {
        return isCardPaused;
    }

    public void setIsCardPaused(Boolean cardPaused) {
        isCardPaused = cardPaused;
    }
}