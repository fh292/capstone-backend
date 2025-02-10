package com.example.capstone.bo;

public class SharedCardResponse {
    private Long id;
    private Long cardId;
    private Long userId;
    private String sharedAt;
    private String expiresAt;

    public SharedCardResponse() {
    }

    public SharedCardResponse(Long id, Long cardId, Long userId, String sharedAt, String expiresAt) {
        this.id = id;
        this.cardId = cardId;
        this.userId = userId;
        this.sharedAt = sharedAt;
        this.expiresAt = expiresAt;
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

    public String getSharedAt() {
        return sharedAt;
    }

    public void setSharedAt(String sharedAt) {
        this.sharedAt = sharedAt;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }
}
