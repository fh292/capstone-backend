package com.example.capstone.bo;

import java.time.LocalDateTime;

import com.example.capstone.entities.TransactionEntity;

public class TransactionResponse {
    private Long id;
    private Long cardId;
    private String merchant;
    private Double amount;
    private Boolean isRecurring;
    private String status;
    private String description;
    private String type;
    private String category;
    private Double longitude;
    private Double latitude;
    private LocalDateTime createdAt;
    private String declineReason;

    public TransactionResponse(TransactionEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.cardId = entity.getCard().getId();
            this.merchant = entity.getMerchant();
            this.amount = entity.getAmount();
            this.isRecurring = entity.getRecurring();
            this.status = entity.getStatus();
            this.description = entity.getDescription();
            this.type = entity.getType();
            this.category = entity.getCategory();
            this.longitude = entity.getLongitude();
            this.latitude = entity.getLatitude();
            this.createdAt = entity.getCreatedAt();
            this.declineReason = entity.getDeclineReason();
        } else {
            this.status = "DECLINED";
        }
    }

    public TransactionResponse(TransactionEntity entity, String declineReason) {
        this(entity);
        this.declineReason = declineReason;
        this.status = "DECLINED";
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
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

    public Boolean getRecurring() {
        return isRecurring;
    }

    public void setRecurring(Boolean recurring) {
        isRecurring = recurring;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getDeclineReason() {
        return declineReason;
    }

    public void setDeclineReason(String declineReason) {
        this.declineReason = declineReason;
    }
}