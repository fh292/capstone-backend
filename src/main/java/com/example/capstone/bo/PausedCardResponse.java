package com.example.capstone.bo;

public class PausedCardResponse {

    private Long cardId;
    private Boolean isPaused;
    private String message;

    public PausedCardResponse(Long cardId, Boolean isPaused, String message) {
        this.cardId = cardId;
        this.isPaused = isPaused;
        this.message = message;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public Boolean getIsPaused() {
        return isPaused;
    }

    public void setIsPaused(Boolean isPaused) {
        this.isPaused = isPaused;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
