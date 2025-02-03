package com.example.capstone.bo;

import com.example.capstone.entities.TransactionEntity;

import java.time.LocalDateTime;

public class TransactionRequest {
    private Long id;
    private Long cardId;
    private Long userId;

    private String type;
    private String status;
    private String merchantName;

    private Double amount;

    private LocalDateTime createdAt;


    //Constructors
    public TransactionRequest() {}

    public TransactionRequest(Long id, Long cardId, Long userId, String type, String status, String merchantName, Double amount, LocalDateTime createdAt) {
        this.id = id;
        this.cardId = cardId;
        this.userId = userId;
        this.type = type;
        this.status = status;
        this.merchantName = merchantName;
        this.amount = amount;
        this.createdAt = createdAt;
    }

    public TransactionRequest(TransactionEntity transactionEntity) {
    }

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
