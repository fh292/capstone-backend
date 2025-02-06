package com.example.capstone.bo;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.capstone.entities.CardEntity;

public class CardResponse {
    protected Long id;
    protected String cardName;
    protected String cardType;
    protected String cardNumber;
    protected String cvv;
    protected LocalDate expiryDate;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
    protected LocalDateTime limitSetAt;
    protected String cardColor;
    protected String cardIcon;
    protected Double per_transaction;
    protected Double per_day;
    protected Double per_week;
    protected Double per_month;
    protected Double per_year;
    protected Double total;
    protected Boolean isPaused;
    protected Boolean isClosed;

    public CardResponse(CardEntity card) {
        this.id = card.getId();
        this.cardName = card.getCardName();
        this.cardType = card.getCardType();
        this.cardNumber = card.getCardNumber();
        this.cvv = card.getCvv();
        this.expiryDate = card.getExpiryDate();
        this.createdAt = card.getCreatedAt();
        this.updatedAt = card.getUpdatedAt();
        this.limitSetAt = card.getLimitSetAt();
        this.cardColor = card.getCardColor();
        this.cardIcon = card.getCardIcon();
        this.per_transaction = card.getPer_transaction();
        this.per_day = card.getPer_day();
        this.per_week = card.getPer_week();
        this.per_month = card.getPer_month();
        this.per_year = card.getPer_year();
        this.total = card.getTotal();
        this.isPaused = card.getPaused();
        this.isClosed = card.getClosed();
    }

    public CardResponse() {

    }

    public Long getId() {
        return id;
    }

    public String getCardName() {
        return cardName;
    }

    public String getCardType() {
        return cardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getLimitSetAt() {
        return limitSetAt;
    }

    public String getCardColor() {
        return cardColor;
    }

    public String getCardIcon() {
        return cardIcon;
    }

    public Double getPer_transaction() {
        return per_transaction;
    }

    public Double getPer_day() {
        return per_day;
    }

    public Double getPer_week() {
        return per_week;
    }

    public Double getPer_month() {
        return per_month;
    }

    public Double getPer_year() {
        return per_year;
    }

    public Double getTotal() {
        return total;
    }

    public Boolean getPaused() {
        return isPaused;
    }

    public Boolean getClosed() {
        return isClosed;
    }
}
