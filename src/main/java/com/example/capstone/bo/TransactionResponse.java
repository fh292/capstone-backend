package com.example.capstone.bo;

import com.example.capstone.entities.TransactionEntity;

import java.time.LocalDateTime;

public class TransactionResponse {
    private Long transactionId;
    private String merchantName;
    private String type;
    private String status;
    private Double amount;
    private String description;
    private LocalDateTime createdAt;

    // Card info
    private Long cardId;
    private String cardType;
    private Boolean cardClosed;
    private Boolean cardPaused;
    private Double remainingLimit;
    private Double perTransactionLimit;
    private String categoryName;


    // User info
    private Long userId;
    private String userName; // or email, or however you identify your User

    public TransactionResponse() {
    }

    public TransactionResponse(Long transactionId, String merchantName, String type, String status, Double amount, String description, LocalDateTime createdAt, Long cardId, String cardType, Boolean cardClosed, Boolean cardPaused, Double remainingLimit, Double perTransactionLimit, Long userId, String userName, String categoryName) {
        this.transactionId = transactionId;
        this.merchantName = merchantName;
        this.type = type;
        this.status = status;
        this.amount = amount;
        this.description = description;
        this.createdAt = createdAt;
        this.cardId = cardId;
        this.cardType = cardType;
        this.cardClosed = cardClosed;
        this.cardPaused = cardPaused;
        this.remainingLimit = remainingLimit;
        this.perTransactionLimit = perTransactionLimit;
        this.userId = userId;
        this.userName = userName;
        this.categoryName = categoryName;
    }

    public TransactionResponse(TransactionEntity transactionEntity) {
        this.transactionId = transactionEntity.getId();
        this.merchantName = transactionEntity.getMerchantName();
        this.type = transactionEntity.getType();
        this.status = transactionEntity.getStatus();
        this.amount = transactionEntity.getAmount();
        this.description = transactionEntity.getDescription();
        this.createdAt = transactionEntity.getCreatedAt();
        this.cardId = transactionEntity.getCard().getId();
        this.cardType = transactionEntity.getCard().getCardType();
        this.cardClosed = transactionEntity.getCard().getIsClosed();
        this.cardPaused = transactionEntity.getCard().getIsPaused();
        this.remainingLimit = transactionEntity.getCard().getRemainingLimit();
        this.perTransactionLimit = transactionEntity.getCard().getPerTransactionLimit();
        this.userId = transactionEntity.getUser().getId();
        this.userName = transactionEntity.getUser().getUsername();
        this.categoryName = transactionEntity.getCard().getCategoryName();}

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public Boolean getCardClosed() {
        return cardClosed;
    }

    public void setCardClosed(Boolean cardClosed) {
        this.cardClosed = cardClosed;
    }

    public Boolean getCardPaused() {
        return cardPaused;
    }

    public void setCardPaused(Boolean cardPaused) {
        this.cardPaused = cardPaused;
    }

    public Double getRemainingLimit() {
        return remainingLimit;
    }

    public void setRemainingLimit(Double remainingLimit) {
        this.remainingLimit = remainingLimit;
    }

    public Double getPerTransactionLimit() {
        return perTransactionLimit;
    }

    public void setPerTransactionLimit(Double perTransactionLimit) {
        this.perTransactionLimit = perTransactionLimit;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
