package com.example.capstone.bo;

import com.example.capstone.entities.SharedCardEntity;

import java.time.LocalDateTime;

public class SharedCardRequest {
    private Long id;
    private Long cardId;
    private Long userId;

    private LocalDateTime sharedAt;
    private LocalDateTime expiresAt;

   public SharedCardRequest() {
    }

    public SharedCardRequest(Long id, Long cardId, Long userId, LocalDateTime sharedAt, LocalDateTime expiresAt) {
        this.id = id;
        this.cardId = cardId;
        this.userId = userId;
        this.sharedAt = sharedAt;
        this.expiresAt = expiresAt;
    }

    public SharedCardRequest(SharedCardEntity sharedCardEntity) {
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

    public LocalDateTime getSharedAt() {
        return sharedAt;
    }

    public void setSharedAt(LocalDateTime sharedAt) {
        this.sharedAt = sharedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
