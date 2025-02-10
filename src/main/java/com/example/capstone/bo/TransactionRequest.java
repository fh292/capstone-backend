package com.example.capstone.bo;

import com.example.capstone.entities.TransactionEntity;

public class TransactionRequest {
    private String cardNumber;
    private String cvv;
    private String expiryDate;
    private String merchant;
    private Double amount;
    private Boolean isRecurring;
    private String description;
    private String type;
    private String category;
    private Double longitude;
    private Double latitude;

    // Default constructor
    public TransactionRequest() {}

    // Constructor from entity (for responses)
    public TransactionRequest(TransactionEntity entity) {
        this.merchant = entity.getMerchant();
        this.amount = entity.getAmount();
        this.isRecurring = entity.getRecurring();
        this.description = entity.getDescription();
        this.type = entity.getType();
        this.category = entity.getCategory();
        this.longitude = entity.getLongitude();
        this.latitude = entity.getLatitude();
    }

    // Getters and Setters
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
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

    public Boolean getIsRecurring() {
        return isRecurring;
    }

    public void setIsRecurring(Boolean recurring) {
        isRecurring = recurring;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
}
